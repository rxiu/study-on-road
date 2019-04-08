package com.rxiu.wechat.core.storage;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.rxiu.wechat.common.util.HttpUtil;
import com.rxiu.wechat.core.PropertyPlaceHolder;

/**
 * @author rxiu
 * @date 2019/4/8
 */
public abstract class AbstractStorage implements Storage {

    private static final long DEFAULT_EXPIRED_TIME = 2 * 60 * 1000;

    protected String accessToken;

    protected long expiredTime;

    @Override
    public String getAccessToken() {
        return getAccessToken(false);
    }

    @Override
    public String getAccessToken(boolean force) {
        if (isAccessTokenExpired() || force) {
            String accessToken = getAccessTokenFromHttp();
            // todo  从配置文件读取过期时间
            updateAccessToken(accessToken);
        }
        return doGetAccessToken();
    }

    protected abstract String doGetAccessToken();

    @Override
    public void updateAccessToken(String accessToken) {
        updateAccessToken(accessToken, DEFAULT_EXPIRED_TIME);
    }

    private String getAccessTokenFromHttp() {
        String accessTokenUrl = PropertyPlaceHolder.getString("wechat.access-token.url")
                .replace("${wechat.app-id}", PropertyPlaceHolder.getString("wechat.app-id"))
                .replace("${wechat.app-secret}", PropertyPlaceHolder.getString("wechat.app-secret"));

        String token = HttpUtil.sendGet(accessTokenUrl, null);

        if (!Strings.isNullOrEmpty(token)) {
            String accessToken = JSONObject.parseObject(token).getString("access_token");
            return accessToken;
        }

        return null;
    }
}
