package com.rxiu.zkui.common;

import java.io.Serializable;

/**
 * @author rxiu
 * @date 2019/4/15
 */
public class ResponseResult implements Serializable {

    public static final String SUCCESS = "0";
    public static final String ERROR = "-1";

    private String code;
    private boolean success;
    private String message;
    private Object data;

    public ResponseResult(String code, boolean success, String message, Object data) {
        this.code = code;
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static ResponseResult success(Object data) {
        return success("", data);
    }

    public static ResponseResult success(String message, Object data) {
        return success(SUCCESS, message, data);
    }

    public static ResponseResult success(String code, String message, Object data) {
        return new ResponseResult(code, true, message, data);
    }


    public static ResponseResult fail(String message) {
        return fail(ERROR, message);
    }

    public static ResponseResult fail(String code, String message) {
        return new ResponseResult(code, false, message, null);
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        success = success;
    }
}
