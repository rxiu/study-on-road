package com.rxiu.security.core.security;

import com.rxiu.security.common.ConstantValue;
import com.rxiu.security.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by rxiu on 2018/3/19.
 */
@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

    @Autowired
    private DaoAuthenticationProvider authenticationProvider;

    @Autowired
    private CustomAccessDeniedHandler handler;

    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        if (configAttributes == null) {
            return;
        }

        if (object instanceof FilterInvocation && "anonymousUser".equals(authentication.getPrincipal())) {
            FilterInvocation fi = (FilterInvocation) object;
            User user = (User) fi.getRequest().getSession().getAttribute(ConstantValue.USER_SESSION);
            if (user != null) {
                Authentication request = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
                authentication = authenticationProvider.authenticate(request);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        Iterator<ConfigAttribute> ite = configAttributes.iterator();

        while (ite.hasNext()) {
            ConfigAttribute ca = ite.next();
            String needRole = ((SecurityConfig) ca).getAttribute();
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                if (needRole.trim().equals(ga.getAuthority().trim())) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("no right.");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
