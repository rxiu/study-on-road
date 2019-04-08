package com.rxiu.hibernate.jdbc.string;

import com.google.common.base.Joiner;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SqlStringConfig {

    private static final Pattern IS_SINGLE_TABLE_NAME_PATTERN = Pattern.compile("^(\\s*?)select\\s(.*?)\\sfrom.*", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

    private final String table;
    private final boolean customSql;
    private final Collection<String> columns;
    private final Collection<String> primaries;

    SqlStringConfig(String table, Collection<String> columns, Collection<String> primaries) {
        this.table = table;
        Matcher matcher = IS_SINGLE_TABLE_NAME_PATTERN.matcher(table);
        this.customSql = matcher.find();
        this.columns = columns;
        this.primaries = primaries;
    }

    public String getTable() {
        return table;
    }

    public Collection<String> getPrimaries() {
        return primaries;
    }

    public String establishQuery() {
        StringBuilder builder = new StringBuilder(2048);
        builder.append("select");

        if (this.columns == null || this.columns.size() <= 0) {
            builder.append(" * ");
        } else {
            String[] columns = this.columns.toArray(new String[this.columns.size()]);

            for (int i = 0; i < columns.length; i++) {
                builder.append(" ").append(columns[i]);
                if (i != columns.length - 1) {
                    builder.append(",");
                }
            }
        }

        builder.append(" from ");

        if (this.customSql) {
            builder.append("(").append(table).append(") mx");
        } else {
            builder.append(table);
        }

        return builder.toString();
    }

    public String establishInsert() {
        if (columns == null || columns.isEmpty()) {
            return null;
        }

        StringBuilder builder = new StringBuilder(2048);
        final int finalSize = columns.size();
        builder.append("insert into ").append(table).append("(").append(Joiner.on(",").join(columns)).append(")")
                .append("values(").append(Joiner.on(",").join(
                (Iterable<String>) () -> new Iterator<String>() {
                    private int size = finalSize;

                    @Override
                    public boolean hasNext() {
                        return size > 0;
                    }

                    @Override
                    public String next() {
                        size--;
                        return "?";
                    }
                }
        )).append(")");

        return builder.toString();
    }

    public String establishUpdate() {
        if (columns == null || columns.isEmpty()) {
            return null;
        }

        StringBuilder builder = new StringBuilder(2048);

        builder.append("update ").append(table).append(" set ");
        int i = 0;
        for (String columnPart : columns) {
            builder.append(columnPart).append(" = ? ");
            if (i < columns.size() - 1) {
                builder.append(",");
                i++;
            }
        }
        builder.append(" where 1 = 1");

        if (this.primaries == null || this.primaries.isEmpty()) return builder.toString();

        for (String primary : this.primaries) {
            builder.append(" and ").append(primary).append(" = ?");
        }
        return builder.toString();
    }

    public String establishDelete() {
        StringBuilder builder = new StringBuilder(2048);
        builder.append("delete from ").append(this.table).append(" where 1 = 1");

        if (this.primaries == null || this.primaries.isEmpty()) return builder.toString();

        for (String primary : this.primaries) {
            builder.append(" and ").append(primary).append(" = ?");
        }
        return builder.toString();
    }
}
