package com.rxiu.wechat.core.storage;

import com.google.common.base.Strings;
import com.rxiu.wechat.common.util.RedisUtil;

/**
 * @author rxiu
 * @date 2019/4/8
 */
public class RedisStorage extends AbstractStorage {
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN_KEY";

    @Override
    protected String doGetAccessToken() {
        return RedisUtil.builder().get(ACCESS_TOKEN);
    }

    @Override
    public void updateAccessToken(String accessToken, long expiredTime) {
        RedisUtil.builder().set(ACCESS_TOKEN, accessToken, expiredTime);
    }

    @Override
    public boolean isAccessTokenExpired() {
        String accessToken = doGetAccessToken();
        return Strings.isNullOrEmpty(accessToken);
    }

    @Override
    public void expiredAccessToken() {
        updateAccessToken(null);
    }
}
