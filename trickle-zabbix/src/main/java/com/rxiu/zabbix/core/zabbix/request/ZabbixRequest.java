package com.rxiu.zabbix.core.zabbix.request;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @author rxiu
 * @date 2018/07/16.
 **/
public class ZabbixRequest {
    private Map<String, Object> params = new HashMap<String, Object>();
    private Integer id = new Random().nextInt(Integer.MAX_VALUE);
    private String jsonrpc = "2.0";
    private String method;
    private String auth;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void param(String key, Object value) {
        params.put(key, value);
    }

    public Object removeParam(String key) {
        return params.remove(key);
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
