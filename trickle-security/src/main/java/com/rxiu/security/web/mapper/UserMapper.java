package com.rxiu.security.web.mapper;

import com.rxiu.security.web.model.User;

/**
 * Created by rxiu on 2018/3/19.
 */
public interface UserMapper extends IBasicMapper<User> {

    User getByUsername(String username);
}
