package com.rxiu.wechat.common.util;

import com.rxiu.wechat.core.SpringContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author rxiu
 * @date 2018/07/20
 */
public class RedisUtil {

    private RedisTemplate<String, String> redisTemplate = SpringContext.getBean(StringRedisTemplate.class);

    private static class Singleton {
        private static final RedisUtil UTIL = new RedisUtil();
    }

    public static RedisUtil builder() {
        return Singleton.UTIL;
    }

    public synchronized void set(final String index, final String value) {
        redisTemplate.opsForValue().set(index, value);
    }

    public synchronized void set(final String index, final String value, long expire) {
        redisTemplate.opsForValue().set(index, value, expire, TimeUnit.MILLISECONDS);
    }

    public synchronized boolean check(final String index) {
        return redisTemplate.hasKey(index);
    }

    public synchronized String getOrDefault(final String index, String defaultValue) {
        if (check(index)) {
            return get(index);
        }
        return defaultValue;
    }

    public synchronized String get(final String index) {
        return redisTemplate.opsForValue().get(index);
    }
}