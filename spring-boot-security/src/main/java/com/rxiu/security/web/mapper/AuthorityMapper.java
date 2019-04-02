package com.rxiu.security.web.mapper;

import com.rxiu.security.web.model.Authority;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by rxiu on 2018/3/19.
 */
public interface AuthorityMapper extends IBasicMapper<Authority> {

    List<Authority> findAll();
    List<Authority> getAllByStream();
    List<Authority> getAuthorityByUserId(@Param("id") Integer id);
}
