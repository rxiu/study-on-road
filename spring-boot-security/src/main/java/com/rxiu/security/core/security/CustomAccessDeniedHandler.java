package com.rxiu.security.core.security;

import com.google.common.base.Strings;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by rxiu on 2018/3/21.
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    String errorPage = "/error";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        boolean isAjax = isAjaxRequest(request);
        if(isAjax){
            String msg = "略略略~~~没权限~!";//accessDeniedException.getMessage();
            response.getWriter().println(msg);
        }else if (!response.isCommitted()) {
            if (errorPage != null) {
                request.setAttribute(WebAttributes.ACCESS_DENIED_403, accessDeniedException);
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                RequestDispatcher dispatcher = request.getRequestDispatcher(errorPage);
                dispatcher.forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException.getMessage());
            }
        }
    }

    private boolean isAjaxRequest(HttpServletRequest request) {
        return !Strings.isNullOrEmpty(request.getHeader("x-requested-with"))
                && request.getHeader("x-requested-with").equals("XMLHttpRequest");
    }
}
