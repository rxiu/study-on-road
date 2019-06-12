package com.rxiu.zkui.core.curator.pool;

/**
 * @author shenyuhang
 * @date 2019/5/28
 **/
public abstract class AbstractPool<T> implements Pool<T> {
    @Override
    public void release(T t) {
        if (validate(t)) {
            doRelease(t);
        } else {
            destroy(t);
        }
    }

    protected abstract boolean validate(T t);

    protected abstract void doRelease(T t);

    protected abstract void destroy(T t);
}
