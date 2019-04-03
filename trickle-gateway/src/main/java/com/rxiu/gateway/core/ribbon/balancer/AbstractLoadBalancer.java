package com.rxiu.gateway.core.ribbon.balancer;

import com.netflix.client.DefaultLoadBalancerRetryHandler;
import com.netflix.client.RetryHandler;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.zuul.context.RequestContext;
import com.rxiu.gateway.common.Constant;
import com.rxiu.gateway.core.SpringContext;
import com.rxiu.gateway.entity.ServerAddress;
import com.rxiu.gateway.service.ServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public abstract class AbstractLoadBalancer implements LoadBalancer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLoadBalancer.class);
    protected ServerService serverService;
    private String name;
    private Map<String, List<String>> unreachableServer = new HashMap<>(256);

    protected AbstractLoadBalancer(String name,Map<String, List<String>> unreachableServer) {
        this.name = name;
        this.unreachableServer = unreachableServer;
    }

    @Override
    public Server chooseServer(ILoadBalancer loadBalancer) {
        this.serverService = SpringContext.getBean(ServerService.class);

        Server server = choose(loadBalancer);
        if (server != null) {
            LOGGER.info(String.format("the server[%s:%s] has been select.", server.getHost(), server.getPort()));
        } else {
            LOGGER.error("could not find server.");
        }
        return server;
    }

    public abstract Server choose(ILoadBalancer loadBalancer);


    private void setMaxAutoRetiresNextServer(int size) {
        SpringClientFactory factory = SpringContext.getBean(SpringClientFactory.class);
        IClientConfig clientConfig = SpringContext.getBean(IClientConfig.class);
        int retrySameServer = clientConfig.get(CommonClientConfigKey.MaxAutoRetries, 0);
        boolean retryEnable = clientConfig.get(CommonClientConfigKey.OkToRetryOnAllOperations, false);
        RetryHandler retryHandler = new DefaultLoadBalancerRetryHandler(retrySameServer, size, retryEnable);
        factory.getLoadBalancerContext(name).setRetryHandler(retryHandler);
    }

    protected List<ServerAddress> filter(List<ServerAddress> addressList) {
        if (addressList == null || addressList.isEmpty()) {
            return null;
        }
        String batchNo = (String) RequestContext.getCurrentContext().get(Constant.REQUEST_BATCH_NO);
        setMaxAutoRetiresNextServer(addressList.size());
        if (!unreachableServer.isEmpty() && !addressList.isEmpty()) {
            List<String> hostAndPorts = unreachableServer.get(batchNo);
            if (hostAndPorts != null && !hostAndPorts.isEmpty()) {
                Iterator<ServerAddress> iter = addressList.iterator();
                while (iter.hasNext()) {
                    ServerAddress hostWeight = iter.next();
                    String hostAndPort = hostWeight.getHost() + Constant.COLON + hostWeight.getPort();
                    if (hostAndPorts.contains(hostAndPort)) {
                        iter.remove();
                    }
                }
            }
        }
        return addressList;
    }
}
