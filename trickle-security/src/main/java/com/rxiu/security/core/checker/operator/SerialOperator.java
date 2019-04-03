package com.rxiu.security.core.checker.operator;

/**
 * @author rxiu
 * @date 2018/09/28.
 **/
public interface SerialOperator {

    /**
     * 获取设备唯一标识
     * @return
     * @throws Exception
     */
    String get(String type) throws Exception;

    /**
     * 保存服务器唯一标识
     * @param code
     * @throws Exception
     */
    void write(String code, String type) throws Exception;
}
