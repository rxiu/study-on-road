package com.rxiu.wechat.core.storage;

/**
 * @author rxiu
 * @date 2019/4/8
 */
public interface Storage {

    /**
     * 获取access_token
     * @return
     */
    String getAccessToken();

    /**
     * 获取access_token
     * @param force 是否强制
     * @return
     */
    String getAccessToken(boolean force);

    /**
     * 更新access_token
     * @param accessToken
     */
    void updateAccessToken(String accessToken);

    /**
     * 更新access_token
     * @param accessToken
     * @param expiredTime 过期时间
     */
    void updateAccessToken(String accessToken, long expiredTime);

    /**
     * access_token是否过期
     * @return
     */
    boolean isAccessTokenExpired();

    /**
     * 强制过期access_token
     */
    void expiredAccessToken();
}
