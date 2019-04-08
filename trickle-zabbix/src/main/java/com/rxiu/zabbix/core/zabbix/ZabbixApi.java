package com.rxiu.zabbix.core.zabbix;

import com.alibaba.fastjson.JSONObject;
import com.rxiu.zabbix.core.zabbix.request.ZabbixRequest;

/**
 * @author rxiu
 * @date 2018/07/16.
 **/
public interface ZabbixApi {
    void init();

    void destroy();

    String apiVersion();

    JSONObject call(ZabbixRequest zabbixRequest);

    JSONObject callWithoutAuth(ZabbixRequest zabbixRequest);

    boolean login(String user, String password);
}
