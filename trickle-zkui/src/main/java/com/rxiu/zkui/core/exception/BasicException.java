package com.rxiu.zkui.core.exception;

/**
 * @author rxiu
 * @date 2019/4/15
 */
public class BasicException extends RuntimeException {

    private String code;
    private String message;

    public BasicException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public BasicException(String message, Object... args) {
        super(String.format(message, args));
    }

    public BasicException(ExceptionResult result) {
        this(result, null);
    }

    public BasicException(ExceptionResult result, Object ...args) {
        setCode(result.getCode());
        setMessage(result.getMessage(args));
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
