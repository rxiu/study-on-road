package com.rxiu.gateway.core.ribbon.balancer;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public interface LoadBalancer {

    /**
     * choose a loadBalancer
     * @param loadBalancer
     * @return
     */
    Server chooseServer(ILoadBalancer loadBalancer);
}
