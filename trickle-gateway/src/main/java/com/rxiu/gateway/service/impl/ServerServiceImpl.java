package com.rxiu.gateway.service.impl;

import com.google.common.collect.Lists;
import com.rxiu.gateway.entity.ServerAddress;
import com.rxiu.gateway.service.ServerService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rxiu
 * @date 2019/4/3
 */
@Service
public class ServerServiceImpl implements ServerService {
    List<ServerAddress> addresses;

    @PostConstruct
    public void createData() {
        addresses = Lists.newArrayListWithExpectedSize(2);
        for (int i = 0; i < 5; i++) {
            ServerAddress address = new ServerAddress();
            address.setHost("127.0.0.1");
            address.setPort(9000 + i);
            address.setWeight(5 + i);
            address.setState(/*(i + 1) % 2*/1);
            addresses.add(address);
        }
    }

    @Override
    public List<ServerAddress> getAvailableServer() {
        return addresses.stream().filter(a -> 1 == a.getState()).collect(Collectors.toList());
    }
}
