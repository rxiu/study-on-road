package com.rxiu.zabbix.core.zabbix;

import com.alibaba.fastjson.JSONObject;
import com.rxiu.zabbix.core.zabbix.request.ZabbixRequest;
import com.rxiu.zabbix.core.zabbix.request.ZabbixRequestBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author rxiu
 * @date 2018/07/16.
 **/
public class DefaultZabbixApi implements ZabbixApi {
    private HttpClient httpClient;

    private String url;

    private String auth;

    public DefaultZabbixApi(String url) {
        this.url = url;
        httpClient = new DefaultHttpClient();
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {
        if (httpClient != null) {
            httpClient = null;
        }
    }

    @Override
    public String apiVersion() {
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .method("apiinfo.version")
                .build();
        JSONObject response = callWithoutAuth(request);
        return response.get("result").toString();
    }

    @Override
    public JSONObject call(ZabbixRequest zabbixRequest) {
        if (zabbixRequest.getAuth() == null) {
            zabbixRequest.setAuth(this.auth);
        }
        return callWithoutAuth(zabbixRequest);
    }

    @Override
    public JSONObject callWithoutAuth(ZabbixRequest zabbixRequest) {
        try {
            HttpPost httpRequest = new HttpPost(url);
            String requestStr = JSONObject.toJSONString(zabbixRequest);
            httpRequest.addHeader("Content-Type", "application/json-rpc");
            httpRequest.setEntity(new StringEntity(requestStr, ContentType.APPLICATION_JSON));
            HttpResponse response = httpClient.execute(httpRequest);
            HttpEntity entity = response.getEntity();
            return JSONObject.parseObject(EntityUtils.toString(entity));
        } catch (IOException e) {
            throw new RuntimeException("DefaultZabbixApi call exception!", e);
        }
    }

    @Override
    public boolean login(String user, String password) {
        this.auth = null;
        ZabbixRequest request = ZabbixRequestBuilder.newBuilder()
                .param("user", user)
                .param("password", password)
                .param("userData", true)
                .method("user.login").build();
        JSONObject response = callWithoutAuth(request);
        Object result = response.get("result");
        if (result != null) {
            Object auth = JSONObject.parseObject(result.toString()).get("sessionid");
            if (auth != null) {
                this.auth = auth.toString();
                return true;
            }
        }
        return false;
    }
}
