package com.rxiu.gateway.core.ribbon.retry.factory;

import com.rxiu.gateway.core.ribbon.retry.policy.ServerRibbonLoadBalancedRetryPolicy;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicy;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancedRetryPolicyFactory;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerContext;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public class ServerRibbonLoadBalancedRetryPolicyFactory extends RibbonLoadBalancedRetryPolicyFactory {
    private SpringClientFactory clientFactory;
    private ServerRibbonLoadBalancedRetryPolicy policy;
    private ServerRibbonLoadBalancedRetryPolicy.RetryTrigger trigger;

    public ServerRibbonLoadBalancedRetryPolicyFactory(SpringClientFactory clientFactory) {
        super(clientFactory);
        this.clientFactory = clientFactory;
    }

    @Override
    public LoadBalancedRetryPolicy create(String serviceId, ServiceInstanceChooser loadBalanceChooser) {
        RibbonLoadBalancerContext lbContext = this.clientFactory
                .getLoadBalancerContext(serviceId);
        policy = new ServerRibbonLoadBalancedRetryPolicy(serviceId, lbContext, loadBalanceChooser, clientFactory.getClientConfig(serviceId));
        if (trigger != null) policy.setTrigger(trigger);
        return policy;
    }

    public void setTrigger(ServerRibbonLoadBalancedRetryPolicy.RetryTrigger trigger) {
        if (policy != null) policy.setTrigger(trigger);
        this.trigger = trigger;
    }
}
