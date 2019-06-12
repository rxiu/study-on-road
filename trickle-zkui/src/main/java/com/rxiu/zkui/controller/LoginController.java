package com.rxiu.zkui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author rxiu
 * @date 2019/4/10
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String login(HttpServletRequest request) {
        return "login";
    }
}
