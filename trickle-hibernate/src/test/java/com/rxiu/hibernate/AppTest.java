package com.rxiu.hibernate;

import com.google.common.collect.Lists;
import com.rxiu.hibernate.common.Context;
import com.rxiu.hibernate.jdbc.session.SqlSession;
import com.rxiu.hibernate.jdbc.session.SqlSessionPool;
import com.rxiu.hibernate.jdbc.string.SqlStringBuilder;
import com.rxiu.hibernate.jdbc.wrapper.Statement;
import com.rxiu.hibernate.jdbc.wrapper.StatementRow;
import com.rxiu.hibernate.model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest {
    SqlSession session;

    Context context;
    SqlStringBuilder builder;

    List<User> records = Lists.newArrayListWithExpectedSize(256);

    @Before
    public void instance() throws Exception {
        context = new Context();
        /*context.put("connection.url", "jdbc:mysql://127.0.0.1:3306/test?serverTimezone=GMT%2b8&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true");
        context.put("connection.username", "root");
        context.put("connection.password", "123456");
        context.put("connection.dialect", "mysql");*/

        context.put("connection.url", "jdbc:oracle:thin:@192.168.1.54:1521:ora11g");
        context.put("connection.username", "ZJK_ZYSTANDARD");
        context.put("connection.password", "ZJK_ZYSTANDARD");
        context.put("connection.dialect", "oracle");
        session = SqlSessionPool.builder().getSqlSession(context);

        builder = new SqlStringBuilder("BASEINFO", session, Lists.newArrayList("id"));

        for (int i = 10001; i <= 100000; i++) {
            User user = new User()
                    .setId(i)
                    .setUsername("username" + i)
                    .setNickname("nickname" + i)
                    .setPassword("123456");

            records.add(user);
        }
    }


    @Test
    public void run() throws Exception {
        //String sql = "select * from security_user where id = ?";
        //	map作为resultClass时 必须指定具体实现类
        //HashMap o = session.executeSingle(sql, HashMap.class, 1);
        //System.out.println(o);

        Long start = System.currentTimeMillis();
/*        List<HashMap> data = session.executeQuery(
                builder.getQuery(),
                0,
                null,
                100,
                HashMap.class,
                null
        );
        System.out.println(data.size() + "\thibernate 查询耗时：" + (( System.currentTimeMillis() - start)));

        //jdbc
        start = System.currentTimeMillis();*/
        session.executeWithTransaction(builder, wrapper -> {
            wrapper.createQueryStatement().executeQuery(3000);
        });
        System.out.println("jdbc 流式查询耗时：" + (( System.currentTimeMillis() - start)));
    }

    private static final Integer batchSize = 3000;
    Collection<StatementRow> rows = Lists.newArrayListWithExpectedSize(batchSize);

    @Test
    public void batchInsert() throws Exception {
        for (User user : records) {
            rows.add(new StatementRow(user));
            if (rows.size() >= batchSize) {
                commit(Statement.StatementMode.INSERT, rows);
            }
        }

        commit(Statement.StatementMode.INSERT, rows);
        rows = null;
        close();
    }

    @Test
    public void batchUpdate() throws Exception {
        for (User user : records) {
            rows.add(new StatementRow(
                    user
                            .setUsername(user.getUsername().toUpperCase())
                            .setNickname(user.getNickname().toUpperCase())
            ));

            if (rows.size() >= batchSize) {
                commit(Statement.StatementMode.UPDATE, rows);
            }
        }

        commit(Statement.StatementMode.UPDATE, rows);
        rows = null;
        close();
    }

    @Test
    public void batchDelete() throws Exception {
        for (User user : records) {
            rows.add(new StatementRow(user));
            if (rows.size() >= batchSize) {
                commit(Statement.StatementMode.DELETE, rows);
            }
        }

        commit(Statement.StatementMode.DELETE, rows);
        rows = null;
        close();
    }

    private void commit(Statement.StatementMode mode, Collection<StatementRow> rows) throws Exception {
        session.executeWithTransaction(builder, wrapper -> {
            switch (mode) {
                case INSERT:
                    wrapper.createInsertStatement().executeBatch(rows);
                    break;
                case UPDATE:
                    wrapper.createUpdateStatement().executeBatch(rows);
                    break;
                case DELETE:
                    wrapper.createDeleteStatement().executeBatch(rows);
                    break;
            }
        });

        rows.forEach(row -> row.clear());
        rows.clear();
    }

    private void close() {
        session.close();

        records.forEach(user -> user = null);
        records.clear();
        records = null;
    }
}
