package com.rxiu.gateway.service.impl;

import com.google.common.collect.Lists;
import com.rxiu.gateway.entity.GatewayAddress;
import com.rxiu.gateway.service.GatewayService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author rxiu
 * @date 2019/4/3
 */
@Service
public class GatewayServiceImpl implements GatewayService {

    private List<GatewayAddress> gateways;

    @PostConstruct
    public void createData() {
        gateways = Lists.newArrayListWithExpectedSize(3);
        for (int i = 0; i < 3; i++) {
            GatewayAddress address = new GatewayAddress();
            address.setId(String.valueOf(i));
            address.setHost("127.0.0.1");
            address.setPort(8090 + i);
            address.setName("测试网关组");
            address.setState(1);
            address.setFkStrategyId(String.valueOf(i % 4));
            gateways.add(address);
        }
    }

    @Override
    public GatewayAddress getByHostAndPort(String host, Integer port) {
        return gateways.stream().filter(a -> host.equals(a.getHost()) && port.compareTo(a.getPort()) == 0).findFirst().orElse(null);
    }
}
