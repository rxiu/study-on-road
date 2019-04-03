package com.rxiu.gateway.core.zuul.router;

import com.rxiu.gateway.core.zuul.entity.BasicRoute;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.List;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public class DataBaseRouter extends AbstractDynamicRouter {

    public DataBaseRouter(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
    }

    @Override
    protected List<BasicRoute> readRoutes() {
        return null;
    }
}
