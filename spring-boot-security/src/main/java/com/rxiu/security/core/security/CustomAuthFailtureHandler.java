package com.rxiu.security.core.security;

import com.rxiu.security.common.ConstantValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by rxiu on 2018/3/21.
 */
public class CustomAuthFailtureHandler implements AuthenticationFailureHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthFailtureHandler.class);
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String message = exception.getMessage();
        if(exception instanceof BadCredentialsException){
            logger.error("用户名或密码错误", message);
        }

        request.getSession().removeAttribute(ConstantValue.USER_SESSION);
        redirectStrategy.sendRedirect(request, response, "/login");
    }
}
