package com.rxiu.elasticsearch.core.elastic;

import com.rxiu.elasticsearch.core.elastic.criterion.ElasticCriterion;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregationBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by rxiu on 2018/06/20.
 **/
public class ElasticQueryBuilder implements ElasticCriterion {

    private List<QueryBuilder> list = new ArrayList<>();

    /**
     * 功能描述：Term 查询
     *
     * @param field 字段名
     * @param value 值
     */
    public ElasticQueryBuilder term(String field, Object value) {
        list.add(new ElasticQueryExpress(field, value, Operator.TERM).toBuilder());
        return this;
    }

    /**
     * 功能描述：Terms 查询
     *
     * @param field  字段名
     * @param values 集合值
     */
    public ElasticQueryBuilder terms(String field, Collection<Object> values) {
        list.add(new ElasticQueryExpress(field, values).toBuilder());
        return this;
    }

    /**
     * 功能描述：fuzzy 查询
     *
     * @param field 字段名
     * @param value 值
     */
    public ElasticQueryBuilder fuzzy(String field, Object value) {
        list.add(new ElasticQueryExpress(field, value, Operator.FUZZY).toBuilder());
        return this;
    }

    /**
     * 功能描述：Range 查询
     *
     * @param from 起始值
     * @param to   末尾值
     */
    public ElasticQueryBuilder range(String field, Object from, Object to) {
        list.add(new ElasticQueryExpress(field, from, to).toBuilder());
        return this;
    }

    /**
     * 功能描述：Range 查询
     *
     * @param queryString 查询语句
     */
    public ElasticQueryBuilder queryString(String queryString) {
        list.add(new ElasticQueryExpress(queryString, Operator.QUERY_STRING).toBuilder());
        return this;
    }

    public AggregationBuilder agg(String name, String field, Projection type) {
        switch (type) {
            case STATS:
                return AggregationBuilders.stats(name).field(field);
            case SUM:
                return AggregationBuilders.sum(name).field(field);
            case AVG:
                return AggregationBuilders.avg(name).field(field);
            case COUNT:
                return AggregationBuilders.count(name).field(field);
            case MIN:
                return AggregationBuilders.min(name).field(field);
            case MAX:
                return AggregationBuilders.max(name).field(field);
            default:
                return AggregationBuilders.stats(name).field(field);
        }
    }

    public DateHistogramAggregationBuilder dateHistogram(String name, String field, DateHistogramInterval interval) {
        return AggregationBuilders.dateHistogram(name).field(field).dateHistogramInterval(interval);
    }

    /**
     * 功能描述：Range 查询
     *
     * @param from 起始值
     * @param to   末尾值
     */
    public RangeAggregationBuilder range(String name, String field, Double from, Double to) {
        return AggregationBuilders.range(name).field(field).addRange(from, to);
    }

    @Override
    public List<QueryBuilder> getBuilderList() {
        return list;
    }
}
