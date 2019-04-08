package com.rxiu.hibernate.jdbc.dialect.pagination;

import org.hibernate.dialect.pagination.LimitHelper;
import org.hibernate.dialect.pagination.TopLimitHandler;
import org.hibernate.engine.spi.RowSelection;

import java.util.Locale;

public class SQLServer2000LimitHandler  extends TopLimitHandler {
    public SQLServer2000LimitHandler(boolean supportsVariableLimit, boolean bindLimitParametersFirst) {
        super(supportsVariableLimit, bindLimitParametersFirst);
    }

    @Override
    public boolean supportsLimit() {
        return false;
    }

    @Override
    public String processSql(String sql, RowSelection selection) {
        if (LimitHelper.hasFirstRow( selection )) {
            throw new UnsupportedOperationException( "query result offset is not supported" );
        }

        final int selectIndex = sql.toLowerCase(Locale.ROOT).indexOf( "select" );
        final int selectDistinctIndex = sql.toLowerCase(Locale.ROOT).indexOf( "select distinct" );
        final int insertionPoint = selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);

        return new StringBuilder( sql.length() + 8 )
                .append( sql )
                .insert( insertionPoint, " TOP (?) " )
                .toString();
    }
}
