package com.rxiu.gateway.common;

import java.util.HashMap;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public class GlobalCache extends HashMap<String, Object> {

    private static class Holder {
        private final static GlobalCache CACHE = new GlobalCache();
    }

    public static GlobalCache instance() {
        return Holder.CACHE;
    }
}
