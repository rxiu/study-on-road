package com.rxiu.zkui.service;

import com.rxiu.zkui.common.PageList;

import java.util.List;

/**
 * @author shenyuhang
 * @date 2019/6/12
 **/
public interface IBaseService<T> {
    PageList<T> getByPage(Integer page, Integer size, T t);

    boolean add(T t);

    T getById(Long id);

    boolean save(T t);

    void delete(T t);

    void delete(Long id);

    boolean deleteByIds(List<Long> ids);
}
