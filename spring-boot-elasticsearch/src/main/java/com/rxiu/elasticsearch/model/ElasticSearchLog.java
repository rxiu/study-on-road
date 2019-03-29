package com.rxiu.elasticsearch.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * Created by rxiu on 2018/3/26.
 */
public class ElasticSearchLog {
    private String id;
    private String message;
    @JSONField(format = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date  createTime;

    public String getId() {
        return id;
    }

    public ElasticSearchLog setId(String id) {
        this.id = id;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ElasticSearchLog setMessage(String message) {
        this.message = message;
        return this;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public ElasticSearchLog setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    @Override
    public String toString() {
        return String.format("=============log:{id:%s, message: %s, create_time: %s}=============", id, message, createTime);
    }
}
