package com.rxiu.hibernate.jdbc.wrapper;

import com.rxiu.hibernate.jdbc.RowHandler;
import com.rxiu.hibernate.jdbc.session.SqlSession;
import com.rxiu.hibernate.jdbc.string.SqlStringBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StatementWrapper {

    private SqlSession session;
    final SqlStringBuilder sqlString;
    final Connection connection;

    public StatementWrapper(SqlSession session, SqlStringBuilder sqlString, Connection connection) {
        this.session = session;
        this.sqlString = sqlString;
        this.connection = connection;
    }

    public Statement createQueryStatement() {
        return new Statement(Statement.StatementMode.QUERY, sqlString, connection, null);
    }

    public Statement createInsertStatement() {
        return new Statement(Statement.StatementMode.INSERT, sqlString, connection, null);
    }

    public Statement createUpdateStatement() {
        return new Statement(Statement.StatementMode.UPDATE, sqlString, connection, (ps, row) -> this.updateRowAction(ps, row));
    }


    private void updateRowAction(final PreparedStatement ps, final StatementRow row) throws SQLException {
        int i = 1, j = 1;

        for (String column : this.sqlString.getColumns()) {
            ps.setObject(i, RowHandler.getValue(row, column));
            i++;
        }

        for (String primary : this.sqlString.getPrimaries()) {

            ps.setObject(row.size() + j, RowHandler.getValue(row, primary));

            j++;
        }
    }

    public Statement createDeleteStatement() {
        return new Statement(Statement.StatementMode.DELETE, sqlString, connection, (ps, row) -> this.deleteRowAction(ps, row));
    }

    private void deleteRowAction(final PreparedStatement ps, final StatementRow row) throws SQLException {
        int i = 1;

        for (String primary : this.sqlString.getPrimaries()) {

            ps.setObject(i, RowHandler.getValue(row, primary));

            i++;
        }
    }
}
