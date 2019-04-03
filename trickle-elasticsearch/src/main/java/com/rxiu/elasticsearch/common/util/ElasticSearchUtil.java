package com.rxiu.elasticsearch.common.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.rxiu.elasticsearch.core.elastic.ElasticQuery;
import com.rxiu.elasticsearch.core.elastic.domain.PageScript;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.bulk.byscroll.BulkByScrollResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by rxiu on 2018/3/26.
 */
public class ElasticSearchUtil {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchUtil.class);
    private static final String DEFAULT_TYPE = "log";
    private static final String ELASTIC_SEARCH_FROM_KEY = "from";
    private static final String ELASTIC_SEARCH_SIZE_KEY = "size";
    private static final Integer ELASTIC_SEARCH_SIZE = 20;
    private static final Integer DEFAULT_SIZE = 100;

    private PreBuiltTransportClient client;

    public ElasticSearchUtil(PreBuiltTransportClient client) {
        this.client = client;
    }

    public boolean add(String index, Map<String, Object> data) throws IOException {
        return add(index, DEFAULT_TYPE, data);
    }

    public boolean add(String index, String type, Map<String, Object> data) throws IOException {
        IndexResponse response = client.prepareIndex(index, type)
                .setSource(serializer(data))
                .get();
        return response.getResult() == DocWriteResponse.Result.CREATED;
    }

    public boolean batchAdd(String index, List<Map<String, Object>> data) {
        return batchAdd(index, DEFAULT_TYPE, data);
    }

    public boolean batchAdd(String index, String type, List<Map<String, Object>> data) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        data.forEach(map -> {
            try {
                bulkRequest.add(client.prepareIndex(index, type).setSource(serializer(map)));
            } catch (IOException e) {
                logger.error("error to batch add data. {}", map);
            }
        });

        BulkResponse response = bulkRequest.execute().actionGet();
        return response.hasFailures();
    }

    private XContentBuilder serializer(Map<String, Object> data) throws IOException {
        if (data == null || data.isEmpty()) {
            return null;
        }

        XContentBuilder builder = jsonBuilder().startObject();

        data.forEach((k, v) -> {
            try {
                builder.field(k, v);
            } catch (IOException e) {
                logger.error("error to serializer field:{}", k, e);
            }
        });

        return builder.endObject();
    }

    public <T> List<T> query(String index, Map<String, Object> params, Class<T> clazz) {
        return query(index, DEFAULT_TYPE, params, null, null, clazz);
    }

    public <T> List<T> query(String index, String type, Map<String, Object> params, Class<T> clazz) {
        return query(index, type, params, null, null, clazz);
    }

    public <T> List<T> query(String index, String type, Map<String, Object> params, Integer from, Integer size, Class<T> clazz) {
        SearchRequestBuilder search = client.prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        search.setQuery(buildQuery(params));

        if (from != null) {
            search.setFrom(from * size);
            updateIndexs(client, index, from, size);
        }

        search.setSize(size == null ? 20 : size < 20 ? 20 : size);

        SearchResponse response = search.get();

        return transform(response.getHits().getHits(), clazz);
    }

    private boolean updateIndexs(TransportClient client, String indices, int from, int size) {
        int records = from * size + size;
        if (records <= 10000) return true;
        UpdateSettingsResponse indexResponse = client.admin().indices()
                .prepareUpdateSettings(indices)
                .setSettings(Settings.builder()
                        .put("index.max_result_window", records)
                        .build()
                ).get();
        return indexResponse.isAcknowledged();
    }

    private <T> List<T> transform(SearchHit[] searchHit, Class<T> clazz) {
        List<T> list = Lists.newArrayListWithExpectedSize(searchHit.length);

        for (SearchHit hit : searchHit) {
            final ObjectMapper mapper = new ObjectMapper();
            list.add(mapper.convertValue(hit.getSource(), clazz));
        }
        return list;
    }

    private BoolQueryBuilder buildQuery(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }

        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        params.forEach((k, v) -> builder.must(QueryBuilders.termQuery(k, v)));
        return builder;
    }

    public boolean deleteData(String index, Map<String, Object> params) {
        final BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(buildQuery(params))
                .source(index)
                .get();

        return response.getDeleted() > 0;
    }

    public boolean deleteIndex(String index) {
        if (indexExists(index)) {
            DeleteIndexResponse response = client.admin()
                    .indices()
                    .delete(new DeleteIndexRequest(index))
                    .actionGet();

            return response.isAcknowledged();
        }
        return true;
    }

    private boolean indexExists(String index) {
        IndicesExistsRequest request = new IndicesExistsRequest(index);
        try {
            IndicesExistsResponse response = client.admin()
                    .indices()
                    .exists(request)
                    .actionGet();
            return response.isExists();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public SearchResponse rangSearch(String index, String type, ElasticQuery constructor, List<AggregationBuilder> agg) throws Exception {
        if (agg == null) {
            return null;
        }
        //排序
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index.split(",")).setTypes(type.split(","));
        if (constructor != null && constructor.getAsc() != null && !constructor.getAsc().isEmpty()) {
            searchRequestBuilder.addSort(constructor.getAsc(), SortOrder.ASC);
        }
        if (constructor != null && constructor.getDesc() != null && !constructor.getDesc().isEmpty()) {
            searchRequestBuilder.addSort(constructor.getDesc(), SortOrder.DESC);
        }
        if (null != constructor) {
            searchRequestBuilder.setQuery(constructor.getBuilder());
        } else {
            searchRequestBuilder.setQuery(QueryBuilders.matchAllQuery());
            constructor = new ElasticQuery();
        }
        int size = constructor.getSize();
        if (size < 0) {
            size = 0;
        }
        if (size > DEFAULT_SIZE) {
            size = DEFAULT_SIZE;
        }        //返回条目数
        searchRequestBuilder.setSize(size);
        searchRequestBuilder.setFrom(constructor.getFrom() < 0 ? 0 : constructor.getFrom());
        for (int i = 0; i < agg.size(); i++) {
            searchRequestBuilder.addAggregation(agg.get(i));
        }
        return searchRequestBuilder.setExplain(true).execute().actionGet();
    }

    public List<Map<String, Object>> searchScript(String index, String type, String script, Map<String, Object> params) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(index.split(",")).setTypes(type.split(","));

        SearchTemplateRequestBuilder builder = new SearchTemplateRequestBuilder(client)
                .setScriptType(ScriptType.INLINE)
                .setScript(parseScript(script))
                .setScriptParams(params)
                .setRequest(searchRequestBuilder.request());

        SearchResponse searchResponse = builder.get().getResponse();
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            result.add(hit.getSource());
        }
        return result;
    }

    private String parseScript(String script) {
        if (Strings.isNullOrEmpty(script)) {
            throw new NullPointerException("elasticsearch script must be specify.");
        }

        PageScript page = JSONObject.parseObject(script, PageScript.class);
        try {
            if (page.getFrom() == null || Integer.parseInt(page.getFrom()) < 0) {
                page.setFrom(String.format("{{%s}}", ELASTIC_SEARCH_FROM_KEY));
            }
        } catch (NumberFormatException e) {
            // ignore
        }

        if (page.getSize() == null) {
            page.setSize(String.format("{{%s}}", ELASTIC_SEARCH_SIZE_KEY));
        }

        return JSONObject.toJSONString(page);
    }
}
