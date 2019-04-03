package com.rxiu.gateway.core;

import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IRule;
import com.netflix.zuul.ZuulFilter;
import com.rxiu.gateway.core.ribbon.ServerLoadBalancerRule;
import com.rxiu.gateway.core.ribbon.retry.factory.ServerRibbonLoadBalancedRetryPolicyFactory;
import com.rxiu.gateway.core.zuul.filter.pre.RateLimiterFilter;
import com.rxiu.gateway.core.zuul.filter.pre.TokenAccessFilter;
import com.rxiu.gateway.core.zuul.filter.pre.UserRightFilter;
import com.rxiu.gateway.core.zuul.router.PropertiesRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicyFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rxiu
 * @date 2019/4/3
 */
@Configuration
public class GatewayConfigure {

    @Autowired
    ZuulProperties zuulProperties;
    @Autowired
    ServerProperties server;
    @Value("${ribbon.client.name:client}")
    private String name;

    public static final int DEFAULT_CONNECT_TIMEOUT = 1000;
    public static final int DEFAULT_READ_TIMEOUT = 1000;

    /**
     * 动态路由
     * @return
     */
    @Bean
    public PropertiesRouter propertiesRouter() {
        return new PropertiesRouter(this.server.getServletPrefix(), this.zuulProperties);
    }

    /**
     * 动态负载
     * @return
     */
    @Bean
    public IRule loadBalance() {
        return new ServerLoadBalancerRule();
    }

    @Bean
    public IClientConfig ribbonClientConfig() {
        DefaultClientConfigImpl config = new DefaultClientConfigImpl();
        config.loadProperties(this.name);
        config.set(CommonClientConfigKey.ConnectTimeout, DEFAULT_CONNECT_TIMEOUT);
        config.set(CommonClientConfigKey.ReadTimeout, DEFAULT_READ_TIMEOUT);
        return config;
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.retry.support.RetryTemplate")
    public LoadBalancedRetryPolicyFactory loadBalancedRetryPolicyFactory(SpringClientFactory clientFactory) {
        return new ServerRibbonLoadBalancedRetryPolicyFactory(clientFactory);
    }

    @Bean
    public ZuulFilter userFilter() {
        return new UserRightFilter();
    }

    @Bean
    public ZuulFilter rateLimiterFilter() {
        return new RateLimiterFilter();
    }

    @Bean
    public ZuulFilter tokenAccessFilter() {
        return new TokenAccessFilter();
    }
}