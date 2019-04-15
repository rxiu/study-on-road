package com.rxiu.zkui.core.exception;

/**
 * @author rxiu
 * @date 2019/4/15
 */
public enum ExceptionResult {
    URL_REDIRECT_ERROR("10000", "地址跳转错误"),
    CHECK_EXCEPTION("20000", "异常错误:%s"),
    PARAMETER_NULL_EXCEPTION("30000", "参数[%s]不能为空!");

    String code, message;
    ExceptionResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage(Object ...args) {
        if (args == null || args.length <= 0) return this.message;

        try {
            return String.format(this.message, args);
        } catch (Exception e) {
            return this.message;
        }
    }
}
