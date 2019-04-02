package com.rxiu.security.core.security;

import com.rxiu.security.common.ConstantValue;
import com.rxiu.security.web.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by rxiu on 2018/3/21.
 */
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthSuccessHandler.class);
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        User user = (User)authentication.getPrincipal();
        request.getSession().setAttribute(ConstantValue.USER_SESSION, user);
        logger.info("登录成功-》{}", user.getUsername());

        response.setContentType("application/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        redirectStrategy.sendRedirect(request, response, "/index");
    }
}
