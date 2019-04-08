package com.rxiu.hibernate;

import com.google.common.collect.Lists;
import com.rxiu.hibernate.common.Context;
import com.rxiu.hibernate.jdbc.session.SqlSession;
import com.rxiu.hibernate.jdbc.session.SqlSessionPool;
import com.rxiu.hibernate.jdbc.string.SqlStringBuilder;
import com.rxiu.hibernate.jdbc.wrapper.Statement;
import com.rxiu.hibernate.jdbc.wrapper.StatementRow;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class FileWriteToDatabase {

    SqlSession session;
    Context context;
    SqlStringBuilder builder;

    private final static String FILE = "g://syhenian//test//source";
    private final static int SIZE = 10000 * 10;
    private final static int BATCH_SIZE = 10000;
    private final static ExecutorService execute = Executors.newFixedThreadPool(3);

    @Before
    public void instance() {
        System.out.println("instance database....");
        context = new Context();
        context.put("connection.url", "jdbc:mysql://127.0.0.1:3306/test?serverTimezone=GMT%2b8&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true");
        context.put("connection.username", "root");
        context.put("connection.password", "123456");
        context.put("connection.dialect", "mysql");
        session = SqlSessionPool.builder().getSqlSession(context);

        builder = new SqlStringBuilder("url", session, Lists.newArrayList("content"));

    }

    @Test
    public void main() throws Exception {
        long lines = Files.lines(Paths.get(FILE)).count();
        long begin = System.currentTimeMillis();
        for (int i = 0; i <= lines / SIZE; i++) {
            if (i < lines / SIZE) {
                execute.execute(new SqlOperator(SIZE * i, SIZE));
            } else if (lines % SIZE > 0) {
                execute.execute(new SqlOperator(SIZE * i, lines % SIZE));
            }
        }
        execute.shutdown();

        while (true) {
            if (execute.isTerminated()) {
                System.out.println(String.format("take times: %sms", (System.currentTimeMillis() - begin)));
                close();
                break;
            }
        }
    }

    private class SqlOperator implements Runnable {
        private long skip;
        private long limit;

        public SqlOperator(long skip, long limit) {
            this.skip = skip;
            this.limit = limit;
        }

        @Override
        public void run() {
            System.out.println(String.format("analyzer lines: %s - %s", skip, (skip / SIZE + 1) * SIZE - 1));
            try {
                List<Map<String, String>> records = Lists.newArrayListWithExpectedSize(SIZE);
                AtomicInteger count = new AtomicInteger(0);
                Files.lines(Paths.get(FILE)).skip(skip).limit(limit).forEach(line -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("content", line);
                    records.add(map);
                    if (count.incrementAndGet() == limit || records.size() >= BATCH_SIZE) {
                        try {
                            batchHandler(records);
                            records.forEach(m -> m.clear());
                            records.clear();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Collection<StatementRow> rows = Lists.newArrayListWithExpectedSize(BATCH_SIZE);

        private void batchHandler(List<Map<String, String>> records) throws Exception {
            for (Map<String, String> record : records) {
                rows.add(new StatementRow(record));
                if (rows.size() >= BATCH_SIZE) {
                    commit(Statement.StatementMode.INSERT, rows);
                }
            }

            commit(Statement.StatementMode.INSERT, rows);
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
    }


    private void close() {
        session.close();
    }
}
