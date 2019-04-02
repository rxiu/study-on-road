package com.rxiu.security.web.response;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author rxiu
 * @date 2018/12/06.
 **/
public class Result implements Serializable {

    private static final long serialVersionUID = -7074012067378557866L;

    /**
     * 返回结果集
     */
    private Object data;

    /**
     * 成功失败
     */
    private boolean success;

    /**
     * 信息
     */
    private String message;

    /**
     * 状态码
     */
    private Integer code;

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
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static Result SUCCESS(Object data) {
        Result result = new Result();
        result.setCode(HttpStatus.OK.value());
        result.setData(data);
        result.setSuccess(true);
        return result;
    }

    public static Result FAILED(String message){
        return FAILED(message, HttpStatus.EXPECTATION_FAILED.value());
    }

    public static Result FAILED(String message, Integer code){
        Result result = new Result();
        result.setCode(code);
        result.setMessage(message);
        result.setSuccess(false);
        return result;
    }
}
