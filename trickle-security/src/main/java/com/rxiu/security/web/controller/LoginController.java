package com.rxiu.security.web.controller;

import com.rxiu.security.common.ConstantValue;
import com.rxiu.security.core.BeanFactory;
import com.rxiu.security.web.mapper.UserMapper;
import com.rxiu.security.web.model.User;
import com.rxiu.security.web.response.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rxiu on 2018/3/20.
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private PasswordEncoder encoder;

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public Result check(HttpServletRequest request, String username, String password) {
        User user =
                BeanFactory.getBean(UserMapper.class).getByUsername(username);
        if (user != null) {
            password = encoder.encode(password);
            if (password.equals(user.getPassword())) {
                request.getSession().setAttribute(ConstantValue.USER_SESSION, user);
                return Result.SUCCESS(String.format("hello, %s", user.getNickname()));
            }
        }
        return Result.FAILED("用户名或密码错误");
    }
}
