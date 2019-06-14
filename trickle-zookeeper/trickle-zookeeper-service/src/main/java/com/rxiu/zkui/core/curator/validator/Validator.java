package com.rxiu.zkui.core.curator.validator;

/**
 * @author shenyuhang
 * @date 2019/5/28
 **/
public interface Validator<T> {

    boolean validate(T t);

    void invalidate(T t);
}
