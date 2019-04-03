package com.rxiu.security.web.mapper;

import com.rxiu.security.web.model.Authority;
import com.rxiu.security.web.model.Role;

import java.util.List;

/**
 * Created by rxiu on 2018/3/19.
 */
public interface RoleMapper extends IBasicMapper<Role> {

    List<Authority> getAuthorityByRole();
}
