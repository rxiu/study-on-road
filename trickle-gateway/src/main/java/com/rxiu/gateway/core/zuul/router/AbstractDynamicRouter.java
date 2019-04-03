package com.rxiu.gateway.core.zuul.router;

import com.rxiu.gateway.core.zuul.entity.BasicRoute;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public abstract class AbstractDynamicRouter extends SimpleRouteLocator implements RefreshableRouteLocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDynamicRouter.class);

    public AbstractDynamicRouter(String servletPath, ZuulProperties properties) {
        super(servletPath, properties);
    }

    @Override
    public void refresh() {
        doRefresh();
    }

    @Override
    protected Map<String, ZuulProperties.ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<String, ZuulProperties.ZuulRoute>();
        routes.putAll(super.locateRoutes());

        List<BasicRoute> results = readRoutes();

        for (BasicRoute result : results) {
            if (StringUtils.isEmpty(result.getPath()) ) {
                continue;
            }
            ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute();
            try {
                BeanUtils.copyProperties(result, zuulRoute);
            } catch (Exception e) {
                LOGGER.error("=============load zuul route info from db with error==============", e);
            }
            routes.put(zuulRoute.getPath(), zuulRoute);
        }
        return routes;
    }

    /**
     * 读取路由信息
     * @return
     */
    protected abstract List<BasicRoute> readRoutes();
}
