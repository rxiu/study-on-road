package com.rxiu.hibernate.jdbc.wrapper;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class StatementRow {
    private final Map<String, Object> row;

    public Map<String, Object> getRow() {
        return row;
    }

    public StatementRow(Object object) {
        this.row = JSONObject.parseObject(JSONObject.toJSONString(object), Map.class);
    }

    public int size() {
        return this.row.size();
    }

    public void clear() {
        this.row.clear();
    }
}
