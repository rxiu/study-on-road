package com.rxiu.zkui.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rxiu.zkui.common.ResponseResult;
import com.rxiu.zkui.core.exception.BasicException;
import com.rxiu.zkui.core.exception.ExceptionResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author rxiu
 * @date 2019/4/10
 */
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {

        if (response.isCommitted()) {
            System.out.println("Can't redirect");
            throw new BasicException(ExceptionResult.URL_REDIRECT_ERROR);
        }

        UserDetails user = (UserDetails) authentication.getPrincipal();
        request.getSession().setAttribute("user", user);
        request.getSession().setAttribute("authRole", user.getAuthorities().toArray());

        response.setContentType("application/json;charset=utf-8");
        ObjectMapper om = new ObjectMapper();
        PrintWriter out = response.getWriter();
        out.write(om.writeValueAsString(ResponseResult.success("登录成功", user)));
        out.flush();
        out.close();
    }
}