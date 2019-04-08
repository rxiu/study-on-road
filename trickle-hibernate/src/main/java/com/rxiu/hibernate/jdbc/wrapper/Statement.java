package com.rxiu.hibernate.jdbc.wrapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rxiu.hibernate.jdbc.RowHandler;
import com.rxiu.hibernate.jdbc.exception.SqlExecutionException;
import com.rxiu.hibernate.jdbc.string.SqlStringBuilder;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Statement {

    private StatementMode mode;
    final SqlStringBuilder sqlString;
    final Connection connection;
    final StatementAction action;

    Statement(StatementMode mode, SqlStringBuilder sqlString, Connection connection, StatementAction action) {
        this.mode = mode;
        this.sqlString = sqlString;
        this.connection = connection;
        this.action = action;
    }

    public void executeQuery(Integer fetchSize) throws SQLException, SqlExecutionException {
        PreparedStatement ps = connection.prepareStatement(getSql(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);

        ps.setFetchSize(fetchSize == null ? Integer.MIN_VALUE : fetchSize);
        ps.setFetchDirection(ResultSet.FETCH_REVERSE);
        ResultSet resultSet = ps.executeQuery();
        ps.setFetchSize(fetchSize);
        ResultSetMetaData md = resultSet.getMetaData(); //获得结果集结构信息,元数据
        int columnCount = md.getColumnCount();

        List<Map<String, Object>> records = Lists.newArrayListWithExpectedSize(256);
        int count = 0;
        while (resultSet.next()) {
            Map<String, Object> data = Maps.newHashMapWithExpectedSize(columnCount);
            if (records.size() % fetchSize == 0) {
                System.out.println(String.format("已抓取%s条数据", count));
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                records.forEach(map -> map.clear());
                records.clear();
            }

            for (int i = 1; i <= columnCount; i++) {
                data.put(md.getColumnName(i), resultSet.getObject(i));
            }
            records.add(data);
            count ++;
        }
        System.out.println(String.format("共抓取%s条数据", count));
    }

    public void executeBatch(final Collection<StatementRow> rows) throws SQLException, SqlExecutionException {
        PreparedStatement ps = connection.prepareStatement(getSql());
        try {
            for (StatementRow row : rows) {

                appendValues(ps, row);
                if (action != null) {
                    action.accept(ps, row);
                }
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception ex) {
            throw new SqlExecutionException(getSql(), ex);
        } finally {
            closePreparedStatement(ps);
        }
    }

    public void execute(final StatementRow row) throws SQLException, SqlExecutionException {
        PreparedStatement ps = connection.prepareStatement(getSql());
        try {
            appendValues(ps, row);
            if (action != null) {
                action.accept(ps, row);
            }
            ps.execute();
        } catch (Exception ex) {
            throw new SqlExecutionException(getSql(), ex);
        } finally {
            closePreparedStatement(ps);
        }
    }

    private void appendValues(final PreparedStatement ps, final StatementRow row) throws SQLException {
        if (this.mode != StatementMode.DELETE) {
            int j = 1;
            for (String column : sqlString.getColumns()) {
                ps.setObject(j, RowHandler.getValue(row, column));
                j++;
            }
        }
    }

    private void closePreparedStatement(PreparedStatement ps) throws SQLException {
        if (!ps.isClosed()) {
            ps.clearParameters();
            ps.clearBatch();
            ps.clearWarnings();
            ps.close();
        }
    }

    private String getSql() {
        switch (this.mode) {
            case DELETE:
                return this.sqlString.getDelete();
            case INSERT:
                return this.sqlString.getInsert();
            case UPDATE:
                return this.sqlString.getUpdate();
            case QUERY:
                return this.sqlString.getQuery();
        }
        return null;
    }

    public enum StatementMode {
        INSERT,
        UPDATE,
        DELETE,
        QUERY;
    }


    @FunctionalInterface
    interface StatementAction {
        void accept(final PreparedStatement ps, final StatementRow row) throws SQLException;
    }
}
