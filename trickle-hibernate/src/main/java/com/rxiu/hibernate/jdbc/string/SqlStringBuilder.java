package com.rxiu.hibernate.jdbc.string;

import com.google.common.collect.Lists;
import com.rxiu.hibernate.jdbc.exception.SqlExecutionException;
import com.rxiu.hibernate.jdbc.session.SqlSession;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by rxiu on 2018/3/29.
 */
public class SqlStringBuilder {

    private final String table;
    private String query;
    private String insert;
    private String update;
    private String delete;
    private Collection<String> columns;
    private SqlStringConfig config;

    public SqlStringBuilder(String table, SqlSession session, Collection<String> primaries) {
        this.table = table;
        try {
            establishMetadata(session);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.config = new SqlStringConfig(table, columns, primaries);

        this.query = this.config.establishQuery();
        this.insert = this.config.establishInsert();
        this.update = this.config.establishUpdate();
        this.delete = this.config.establishDelete();
    }

    public String getQuery() {
        return query;
    }

    public String getInsert() {
        return insert;
    }

    public String getUpdate() {
        return update;
    }

    public String getDelete() {
        return delete;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setInsert(String insert) {
        this.insert = insert;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public Collection<String> getColumns() {
        return columns;
    }

    public Collection<String> getPrimaries() {
        return config.getPrimaries();
    }

    public void establishMetadata(SqlSession session) throws SQLException, SqlExecutionException {

        StringBuilder builder = new StringBuilder(256)
                .append("select * from (")
                .append(" select * from ").append(table)
                .append(") m where 1 <> 1");

        Session sessionReal = session.createSession();

        String sqlString = builder.toString();
        builder = null;

        ResultSet set = null;
        try {
            set = sessionReal.doReturningWork(connection -> {
                PreparedStatement preparedStatement = connection.prepareStatement(sqlString);
                return preparedStatement.executeQuery();
            });
            ResultSetMetaData metaData = set.getMetaData();

            this.columns = Lists.newLinkedList();

            for (int i = 0, size = metaData.getColumnCount(); i < size; i++) {
                String columnName = metaData.getColumnName(i + 1);
                this.columns.add(columnName);
            }

        } catch (Exception e) {
            throw new SqlExecutionException(sqlString, e);
        } finally {
            if (set != null && !set.isClosed()) {
                set.clearWarnings();
                set.close();
                set = null;
            }
            sessionReal.close();
            sessionReal = null;
        }
    }
}
