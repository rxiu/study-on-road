package com.rxiu.gateway.core.ribbon.balancer;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.List;
import java.util.Map;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public class RandomLoadBalancer extends AbstractLoadBalancer {
    public RandomLoadBalancer(String name, Map<String, List<String>> unreachableServer) {
        super(name, unreachableServer);
    }

    @Override
    public Server choose(ILoadBalancer loadBalancer) {
        return null;
    }
}
