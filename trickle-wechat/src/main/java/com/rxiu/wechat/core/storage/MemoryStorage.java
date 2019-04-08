package com.rxiu.wechat.core.storage;

/**
 * @author rxiu
 * @date 2019/4/8
 */
public class MemoryStorage extends AbstractStorage{

    @Override
    public boolean isAccessTokenExpired() {
        return this.expiredTime <= 0 || System.currentTimeMillis() > this.expiredTime;
    }

    @Override
    public void expiredAccessToken() {
        this.expiredTime = 0;
    }

    @Override
    protected String doGetAccessToken() {
        return this.accessToken;
    }

    @Override
    public void updateAccessToken(String accessToken, long expiredTime) {
        this.accessToken = accessToken;
        this.expiredTime = System.currentTimeMillis() + (expiredTime - 200) * 1000;
    }
}
