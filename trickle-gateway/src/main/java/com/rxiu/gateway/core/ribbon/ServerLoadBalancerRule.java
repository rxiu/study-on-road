package com.rxiu.gateway.core.ribbon;

import com.google.common.base.Preconditions;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.zuul.context.RequestContext;
import com.rxiu.gateway.common.Constant;
import com.rxiu.gateway.common.util.SystemUtil;
import com.rxiu.gateway.core.ribbon.balancer.LoadBalancer;
import com.rxiu.gateway.core.ribbon.balancer.RandomLoadBalancer;
import com.rxiu.gateway.core.ribbon.balancer.RoundLoadBalancer;
import com.rxiu.gateway.core.ribbon.retry.factory.ServerRibbonLoadBalancedRetryPolicyFactory;
import com.rxiu.gateway.core.ribbon.retry.policy.ServerRibbonLoadBalancedRetryPolicy;
import com.rxiu.gateway.entity.GatewayAddress;
import com.rxiu.gateway.service.GatewayService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryContext;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryPolicyFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public class ServerLoadBalancerRule extends AbstractLoadBalancerRule implements ServerRibbonLoadBalancedRetryPolicy.RetryTrigger {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerLoadBalancerRule.class);

    @Value("${server.host:127.0.0.1}")
    private String host;
    @Value("${server.port:8080}")
    private Integer port;

    @Autowired
    private GatewayService gatewayService;
    /**
     * 不可用的服务器
     */
    private Map<String, List<String>> unreachableServer = new HashMap<>(256);
    /**
     * 上一次请求标记
     */
    private String lastRequest;

    @Value("${ribbon.client.name:client}")
    private String name;

    @Autowired
    LoadBalancedRetryPolicyFactory policyFactory;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    @Override
    public Server choose(Object key) {
        retryTrigger();
        return getServer(getLoadBalancer(), key);
    }

    private Server getServer(ILoadBalancer loadBalancer, Object key) {
        if (StringUtils.isBlank(host)) {
            host = SystemUtil.ipList().get(0);
        }
        Preconditions.checkArgument(host != null, "server.host must be specify.");
        Preconditions.checkArgument(port != null, "server.port must be specify.");

        GatewayAddress address = gatewayService.getByHostAndPort(host, port);
        if (address == null) {
            LOGGER.error(String.format("must be config a gateway address for the server[%s:%s].", host, String.valueOf(port)));
            return null;
        }

        LoadBalancer balancer = LoadBalancerFactory.build(address.getFkStrategyId(), name,unreachableServer);

        return balancer.chooseServer(loadBalancer);
    }

    private void retryTrigger() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String batchNo = (String) ctx.get(Constant.REQUEST_BATCH_NO);
        if (!isLastRequest(batchNo)) {
            //不是同一次请求，清理所有缓存的不可用服务
            unreachableServer.clear();
        }

        if (policyFactory instanceof ServerRibbonLoadBalancedRetryPolicyFactory) {
            ((ServerRibbonLoadBalancedRetryPolicyFactory) policyFactory).setTrigger(this);
        }
    }

    private boolean isLastRequest(String batchNo) {
        return batchNo != null && batchNo.equals(lastRequest);
    }

    @Override
    public void exec(LoadBalancedRetryContext context) {
        RequestContext ctx = RequestContext.getCurrentContext();
        String batchNo = (String) ctx.get(Constant.REQUEST_BATCH_NO);
        lastRequest = batchNo;
        List<String> hostAndPorts = unreachableServer.get((String) ctx.get(Constant.REQUEST_BATCH_NO));
        if (hostAndPorts == null) {
            hostAndPorts = new ArrayList<>();
        }
        if (context != null && context.getServiceInstance() != null) {
            String host = context.getServiceInstance().getHost();
            int port = context.getServiceInstance().getPort();
            if (!hostAndPorts.contains(host + Constant.COLON + port))
                hostAndPorts.add(host + Constant.COLON + port);
            unreachableServer.put((String) ctx.get(Constant.REQUEST_BATCH_NO), hostAndPorts);
        }
    }

    static class LoadBalancerFactory {

        public static LoadBalancer build(String strategy, String name, Map<String, List<String>> unreachableServer) {
            GatewayAddress.StrategyType type = GatewayAddress.StrategyType.of(strategy);
            switch (type) {
                case ROUND:
                    return new RoundLoadBalancer(name, unreachableServer);
                case RANDOM:
                    return new RandomLoadBalancer(name, unreachableServer);
                default:
                    return null;
            }
        }
    }
}
