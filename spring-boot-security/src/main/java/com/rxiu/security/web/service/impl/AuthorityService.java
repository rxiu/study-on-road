package com.rxiu.security.web.service.impl;

import com.rxiu.security.web.mapper.AuthorityMapper;
import com.rxiu.security.web.model.Authority;
import com.rxiu.security.web.service.IAuthorityService;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by rxiu on 2018/3/19.
 */
@Service
public class AuthorityService implements IAuthorityService {

    @Autowired
    AuthorityMapper mapper;

    @Autowired
    SqlSessionTemplate template;

    @Override
    public List<Authority> findAll() {
        /**
         * mybatis流式读取大量数据
         * mapper.xml文件中需指定fetchSize
         */
        template.select("com.rxiu.security.web.mapper.AuthorityMapper.getAllByStream", new ResultHandler() {
            @Override
            public void handleResult(ResultContext resultContext) {
                Authority authority = (Authority)resultContext.getResultObject();
                System.out.println(resultContext.getResultCount() + "\t\t\t" + authority.getName());
            }
        });

        return mapper.findAll();
    }
}
