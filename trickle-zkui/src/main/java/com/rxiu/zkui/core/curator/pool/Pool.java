package com.rxiu.zkui.core.curator.pool;

/**
 * @author shenyuhang
 * @date 2019/5/28
 **/
public interface Pool<T> {
    void put(T t);
    T get();

    void release(T t);

    void shutdown();
}
