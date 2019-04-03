package com.rxiu.elasticsearch.core.elastic;

import com.rxiu.elasticsearch.core.elastic.criterion.*;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

/**
 * Created by rxiu on 2018/06/20.
 **/
public class ElasticQuery {

    private int size = Integer.MAX_VALUE;
    private int from = 0;
    private String asc;
    private String desc;
    private final MustCriterion mustCriterion;
    private final FilterCriterion filterCriterion;
    private final MustNotCriterion mustNotCriterion;
    private final ShouldCriterion shouldCriterion;

    public ElasticQuery() {
        mustCriterion = new MustCriterion();
        filterCriterion = new FilterCriterion();
        mustNotCriterion = new MustNotCriterion();
        shouldCriterion = new ShouldCriterion();
    }

    public QueryBuilder getBuilder() {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        QueryBuilder queryBuilder = null;

        for (ElasticCriterion criterion : mustCriterion) {
            for (QueryBuilder builder : criterion.getBuilderList()) {
                queryBuilder = boolQueryBuilder.must(builder);
            }
        }

        return queryBuilder;
    }

    /**
     * 增加简单条件表达式
     */
    public ElasticQuery must(ElasticCriterion criterion) {
        if (criterion != null) {
            mustCriterion.add(criterion);
        }
        return this;
    }

    /**
     * 增加简单条件表达式
     */
    public ElasticQuery filter(ElasticCriterion criterion) {
        if (criterion != null) {
            filterCriterion.add(criterion);
        }
        return this;
    }

    /**
     * 增加简单条件表达式
     */
    public ElasticQuery should(ElasticCriterion criterion) {
        if (criterion != null) {
            shouldCriterion.add(criterion);
        }
        return this;
    }

    /**
     * 增加简单条件表达式
     */
    public ElasticQuery mustNot(ElasticCriterion criterion) {
        if (criterion != null) {
            mustNotCriterion.add(criterion);
        }
        return this;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public String getAsc() {
        return asc;
    }

    public void setAsc(String asc) {
        this.asc = asc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
