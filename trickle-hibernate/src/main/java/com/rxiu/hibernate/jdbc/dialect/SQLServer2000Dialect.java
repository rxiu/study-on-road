package com.rxiu.hibernate.jdbc.dialect;

import com.rxiu.hibernate.jdbc.dialect.pagination.SQLServer2000LimitHandler;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.dialect.pagination.LimitHandler;

public class SQLServer2000Dialect extends SQLServerDialect {

    public SQLServer2000Dialect() {
        super();
    }

    @Override
    public LimitHandler getLimitHandler() {
        return new SQLServer2000LimitHandler(false, false);
    }
}