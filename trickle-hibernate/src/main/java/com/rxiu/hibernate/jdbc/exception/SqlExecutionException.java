package com.rxiu.hibernate.jdbc.exception;

public class SqlExecutionException extends RuntimeException {

    public SqlExecutionException(String sqlString, Throwable cause) {
        super(String.format("%s: %s", cause.getMessage(), sqlString), cause);
    }
}
