package com.rxiu.zkui.core.curator.pool;

import java.util.concurrent.TimeUnit;

/**
 * @author shenyuhang
 * @date 2019/5/28
 **/
public interface BlockingPool<T> extends Pool<T> {

    T get(Long time, TimeUnit unit);
}
