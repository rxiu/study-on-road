package com.rxiu.hibernate.jdbc;

import com.rxiu.hibernate.jdbc.wrapper.StatementRow;

import java.util.Map;

public final class RowHandler {

    public static Object getValue(StatementRow row, String colName) {
        return getValue(row.getRow(), colName);
    }

    public static Object getValue(Map<String, Object> row, String colName) {
        return row.getOrDefault(colName, null);
    }
}
