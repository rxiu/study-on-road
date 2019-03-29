package com.rxiu.elasticsearch.core.elastic;

import com.rxiu.elasticsearch.core.elastic.criterion.ElasticCriterion;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Collection;

/**
 * Created by rxiu on 2018/06/20.
 **/
public class ElasticQueryExpress {

    private String fieldName;
    private Object value;
    private Collection<Object> values;
    private ElasticCriterion.Operator operator;
    private Object from;
    private Object to;

    ElasticQueryExpress(String fieldName, Object value, ElasticCriterion.Operator operator) {
        this.fieldName = fieldName;
        this.value = value;
        this.operator = operator;
    }

    ElasticQueryExpress(String value, ElasticCriterion.Operator operator) {
        this.value = value;
        this.operator = operator;
    }

    ElasticQueryExpress(String fieldName, Collection<Object> values) {
        this.fieldName = fieldName;
        this.values = values;
        this.operator = ElasticCriterion.Operator.TERMS;
    }

    ElasticQueryExpress(String fieldName, Object from, Object to) {
        this.fieldName = fieldName;
        this.from = from;
        this.to = to;
        this.operator = ElasticCriterion.Operator.RANGE;
    }

    QueryBuilder toBuilder() {
        QueryBuilder qb = null;
        switch (operator) {
            case TERM:
                qb = QueryBuilders.termQuery(fieldName, value);
                break;
            case TERMS:
                qb = QueryBuilders.termsQuery(fieldName, values);
                break;
            case RANGE:
                qb = QueryBuilders.rangeQuery(fieldName).from(from).to(to).includeLower(true).includeUpper(true);
                break;
            case FUZZY:
                qb = QueryBuilders.fuzzyQuery(fieldName, value);
                break;
            case QUERY_STRING:
                qb = QueryBuilders.queryStringQuery(value.toString());
            default:

        }
        return qb;
    }
}
