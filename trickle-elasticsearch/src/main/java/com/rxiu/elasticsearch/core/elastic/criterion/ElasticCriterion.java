package com.rxiu.elasticsearch.core.elastic.criterion;

import org.elasticsearch.index.query.QueryBuilder;

import java.util.List;

/**
 * Created by rxiu on 2018/06/20.
 **/
public interface ElasticCriterion {

    /**
     * return all query builder
     * @return
     */
    List<QueryBuilder> getBuilderList();

    enum Operator {
        TERM, TERMS, RANGE, FUZZY, QUERY_STRING, MISSING
    }

    enum MatchMode {
        START, END, ANYWHERE
    }

    enum Projection {
        MAX, MIN, AVG, LENGTH, SUM, COUNT, STATS
    }
}
