package com.rxiu.gateway.service;


import com.rxiu.gateway.entity.ServerAddress;

import java.util.List;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public interface ServerService {
    /**
     * get alive server
     * @return
     */
    List<ServerAddress> getAvailableServer();
}
