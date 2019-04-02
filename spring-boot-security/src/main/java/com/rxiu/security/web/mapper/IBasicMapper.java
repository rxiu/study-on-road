package com.rxiu.security.web.mapper;

import com.rxiu.security.web.model.BaseEntity;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by rxiu on 2018/3/19.
 */
public interface IBasicMapper<T extends BaseEntity> extends Mapper<T> {
    int addList(List<T> var1) throws Exception;

    boolean updateList(List<T> var1) throws Exception;

    int deleteByIds(String[] var1) throws Exception;
}