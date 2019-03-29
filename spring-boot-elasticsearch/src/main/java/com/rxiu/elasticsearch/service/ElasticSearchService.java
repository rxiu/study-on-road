package com.rxiu.elasticsearch.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rxiu.elasticsearch.core.elastic.ElasticQuery;
import com.rxiu.elasticsearch.core.elastic.ElasticQueryBuilder;
import com.rxiu.elasticsearch.model.ElasticSearchLog;
import com.rxiu.elasticsearch.common.util.ElasticSearchUtil;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.ExtendedBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by rxiu on 2018/3/26.
 */
@Service
public class ElasticSearchService {

    @Autowired
    PreBuiltTransportClient client;

    ElasticSearchUtil util;

    @PostConstruct
    public void instance() {
        util = new ElasticSearchUtil(client);
    }

    public boolean batchAdd(String index, List<Map<String, Object>> data) {
        return util.batchAdd(index, data);
    }

    public List<ElasticSearchLog> query(String index, Map<String, Object> params) {
        return util.query(index, params, ElasticSearchLog.class);
    }

    public boolean deleteData(String index, Map<String, Object> params) {
        return util.deleteData(index, params);
    }

    public boolean deleteIndex(String index) {
        return util.deleteIndex(index);
    }

    public Object range(String index, String type, Date startTime, Date endTime) {
        try {
            List<Map<String, Object>> result = null;
            ElasticQuery query = new ElasticQuery();
            ElasticQueryBuilder builder = new ElasticQueryBuilder();
            builder.term("restfulId.keyword", "66d6c06aae6f4f52bbbcc22bf17dd762");
            builder.range("callTime", startTime.getTime(), endTime.getTime());
            query.must(builder);

            DateHistogramAggregationBuilder dateAgg = builder.dateHistogram("date_agg", "callTime", DateHistogramInterval.DAY)
                    // 东八区误差
                    .offset(-(8 * 60 * 60 * 1000))
                    // 设置空桶
                    .minDocCount(0L)
                    .extendedBounds(new ExtendedBounds(startTime.getTime(), endTime.getTime()))
                    // 子聚合
                    .subAggregation(
                            AggregationBuilders.terms("http_state_agg").field("httpState").size(20)
                    );

            SearchResponse response = util.rangSearch(index, type, query, Lists.newArrayList(dateAgg));
            if (response != null) {
                Aggregation aggregation = response.getAggregations().get(dateAgg.getName());
                List<InternalDateHistogram.Bucket> buckets = ((InternalDateHistogram) aggregation).getBuckets();
                result = Lists.newArrayListWithExpectedSize(buckets.size());
                if (buckets != null && buckets.size() > 0) {
                    for (InternalDateHistogram.Bucket bucket : buckets) {
                        Map<String, Object> map = Maps.newHashMapWithExpectedSize(5);
                        map.put("key", formatDate(bucket.getKeyAsString()));
                        map.put("keyAsString", bucket.getKeyAsString());
                        map.put("count", bucket.getDocCount());
                        LongTerms httpStateAgg = bucket.getAggregations().get("http_state_agg");
                        if (httpStateAgg != null) {
                            List<Map<String, Object>> subBucket = Lists.newArrayListWithExpectedSize(buckets.size());
                            for (Terms.Bucket hBucket : httpStateAgg.getBuckets()) {
                                Map<String, Object> subMap = Maps.newHashMapWithExpectedSize(5);
                                subMap.put("key", hBucket.getKeyAsString());
                                subMap.put("keyAsString", hBucket.getKeyAsString());
                                subMap.put("count", hBucket.getDocCount());
                                subBucket.add(subMap);
                            }
                            map.put("httpStateAgg", subBucket);
                        }
                        result.add(map);
                    }
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List searchScript(String index, String type, String script, Map<String, Object> params) {
        try {
            return util.searchScript(index, type, script, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String formatDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long lt = new Long(date);
            return simpleDateFormat.format(new Date(lt));
        } catch (NumberFormatException ex) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            try {
                Date newDate = sdf.parse(date);
                return simpleDateFormat.format(newDate);
            } catch (ParseException e) {
                return date;
            }
        }
    }
}
