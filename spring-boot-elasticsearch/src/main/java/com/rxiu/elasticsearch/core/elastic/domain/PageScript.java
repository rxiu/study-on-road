package com.rxiu.elasticsearch.core.elastic.domain;

import java.util.Map;

/**
 * @author rxiu
 * @date 2018/08/06.
 **/
public class PageScript implements Script {
    private String from;
    private String size;
    private Map<String, Object> query;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public Map<String, Object> getQuery() {
        return query;
    }

    public void setQuery(Map<String, Object> query) {
        this.query = query;
    }
}
