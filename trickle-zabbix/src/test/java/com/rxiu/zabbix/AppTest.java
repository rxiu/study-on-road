package com.rxiu.zabbix;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.rxiu.zabbix.core.PropertyPlaceHolder;
import com.rxiu.zabbix.core.zabbix.DefaultZabbixApi;
import com.rxiu.zabbix.core.zabbix.ZabbixApi;
import com.rxiu.zabbix.core.zabbix.request.ZabbixRequest;
import com.rxiu.zabbix.core.zabbix.request.ZabbixRequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest {

    ZabbixApi zabbixApi;

    @Before
    public void init() {
        PropertyPlaceHolder.load("application.properties");

        zabbixApi = new DefaultZabbixApi(PropertyPlaceHolder.getString("zabbix.url"));
        zabbixApi.login(PropertyPlaceHolder.getString("zabbix.user"),
                PropertyPlaceHolder.getString("zabbix.pswd"));
    }


    @Test
    public void run() {
        //region 1 api version
        System.out.println(zabbixApi.apiVersion());

        //region 2 getAllHost
        Map map = new HashMap();
        map.put("status", new Integer[]{0, 1});

        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("host.get")
                .param("filter", map)
                .build();

        JSONObject json = zabbixApi.call(request);

        String hostId = "";
        if (json.get("result") != null) {
            List hosts = JSONObject.parseArray(json.get("result").toString());
            if (hosts != null && !hosts.isEmpty()) {
                if (JSONObject.parseObject(hosts.get(0).toString()).get("hostid") != null) {
                    hostId =  JSONObject.parseObject(hosts.get(0).toString()).get("hostid").toString();
                }
            }
        }
        System.out.println(hostId);

        //region 3 createweb scenario
        map.clear();
        map.put("name", "homepage");
        map.put("url", "https://www.baidu.com/");
        map.put("status_codes", 200);
        request = ZabbixRequestBuilder.newBuilder()
                .method("httptest.create")
                .param("name", "home page check")
                .param("hostid", hostId)
                .param("steps", Lists.newArrayList(map))
                .build();
        json = zabbixApi.call(request);
        String httpTestId = "";
        if (json != null && json.get("result") != null) {
            List httpTestIds = JSONObject.parseArray(json.get("result").toString());//.get("httptestids");
            if (httpTestIds != null && !httpTestIds.isEmpty()) {
                httpTestId = httpTestIds.get(0).toString();
            }
        }

        //region 4 get web scenario
        request = ZabbixRequestBuilder.newBuilder()
                .method("httptest.get")
                .param("httptestids", new String[]{"99"})
                .build();

        json = zabbixApi.call(request);
        if (json.get("result") != null) {
            List scenarios = JSONObject.parseArray(json.get("result").toString());
            if (scenarios != null && !scenarios.isEmpty()) {
                if (JSONObject.parseObject(scenarios.get(0).toString()).get("hostid") != null) {
                    hostId =  JSONObject.parseObject(scenarios.get(0).toString()).get("hostid").toString();
                }
            }
        }

        //region 5 get history
        request = ZabbixRequestBuilder.newBuilder()
                .method("history.get")
                .param("sortorder", "desc")
                .param("sortfield", "clock")
                .param("history", 0)
                .param("hostids", new String[]{hostId})
                .param("limit", 20)
                //.param("time_from", new String[]{hostId})
                //.param("time_till", new String[]{hostId})
                .build();

        json = zabbixApi.call(request);
        if (json.get("result") != null) {
        }

    }
}
