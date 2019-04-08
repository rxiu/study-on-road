package com.rxiu.hibernate;

import com.google.common.collect.Lists;
import com.rxiu.hibernate.common.Context;
import com.rxiu.hibernate.jdbc.session.SqlSession;
import com.rxiu.hibernate.jdbc.session.SqlSessionPool;
import com.rxiu.hibernate.jdbc.string.SqlStringBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

public class SQLServer2000Tests {

    SqlSession session;

    Context context;
    SqlStringBuilder builder;

    @Before
    public void instance() throws Exception {
        context = new Context();

        context.put("connection.url", "jdbc:sqlserver://192.168.2.173:1433;DatabaseName=dbtest;SelectMethod=Cursor");
        context.put("connection.username", "sa");
        context.put("connection.password", "zysoft@173");
        context.put("connection.dialect", "mssql");
        session = SqlSessionPool.builder().getSqlSession(context);

        builder = new SqlStringBuilder("test", session, Lists.newArrayList("id"));
    }


    @Test
    public void run() throws Exception {
        Long start = System.currentTimeMillis();
        List<HashMap> data = session.executeQuery(
                builder.getQuery(),
                0,
                100,
                100,
                HashMap.class,
                null
        );
        System.out.println(data.size() + "\thibernate 查询耗时：" + (( System.currentTimeMillis() - start)));
    }
}
