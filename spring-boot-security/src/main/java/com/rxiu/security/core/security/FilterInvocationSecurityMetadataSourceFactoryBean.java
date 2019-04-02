package com.rxiu.security.core.security;

import com.google.common.base.Strings;
import com.rxiu.security.web.model.Authority;
import com.rxiu.security.web.service.impl.AuthorityService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.DefaultFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by rxiu on 2018/3/20.
 */
@Component
public class FilterInvocationSecurityMetadataSourceFactoryBean implements FactoryBean<FilterInvocationSecurityMetadataSource> {

    @Autowired
    private AuthorityService authorityService;

    @Override
    public FilterInvocationSecurityMetadataSource getObject() throws Exception {
        return new DefaultFilterInvocationSecurityMetadataSource(getResources());
    }

    @Override
    public Class<FilterInvocationSecurityMetadataSource> getObjectType() {
        return FilterInvocationSecurityMetadataSource.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    protected LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> getResources() {
        LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>> resourceMap = new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();

        List<Authority> resourceList = authorityService.findAll();
        if (resourceList != null) {
            for (Authority authority : resourceList) {
                if (!Strings.isNullOrEmpty(authority.getUrl())) {
                    String url = null;
                    Authority.TYPE type = Authority.TYPE.forValue(authority.getType());
                    if (Authority.TYPE.DIR.equals(type)) {
                        url = authority.getUrl();
                    } else if (Authority.TYPE.OPERATE.equals(type)) {
                        /*String menuPath = authority.getParent().getUrl();
                        int index = menuPath.lastIndexOf("?method=");
                        if(index > -1){
                            url = menuPath.subSequence(0, index) + "?method=" + authority.getUrl();
                        }else{
                            url = menuPath + "?method=" + authority.getUrl();
                        }*/

                    }
                    String role = "AUTH_" + authority.getId();
                    RequestMatcher matcher = new AntPathRequestMatcher(url);
                    Collection<ConfigAttribute> attributes = resourceMap.get(matcher);

                    if (attributes == null) {
                        attributes = new HashSet<ConfigAttribute>();
                        resourceMap.put(matcher, attributes);
                    }

                    attributes.add(new SecurityConfig(role));
                }
            }
        }

        return resourceMap;

    }

}