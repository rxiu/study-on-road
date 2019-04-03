package com.rxiu.security.web.service;

import com.rxiu.security.web.model.Authority;

import java.util.List;

/**
 * Created by rxiu on 2018/3/19.
 */
public interface IAuthorityService {

    List<Authority> findAll();
}
