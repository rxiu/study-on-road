package com.rxiu.security.core.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author rxiu
 * @date 2018/12/06.
 **/
public class CrossOriginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        HttpServletRequest request=(HttpServletRequest)servletRequest;

        //不用通配符*，解决跨域session问题。
        httpResponse.setHeader("Access-Control-Allow-Origin", /*,"*"*/request.getHeader("Origin"));
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

        httpResponse.setHeader("Access-Control-Allow-Headers", "X-Requested-With,X_Requested_With");
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        httpResponse.setHeader("Pragma", "No-cache");
        httpResponse.setHeader("Cache-Control", "no-cache");

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
