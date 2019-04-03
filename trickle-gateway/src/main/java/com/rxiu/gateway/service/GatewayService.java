package com.rxiu.gateway.service;


import com.rxiu.gateway.entity.GatewayAddress;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public interface GatewayService {

    /**
     * 获取网关信息
     * @param host
     * @param port
     * @return
     */
    GatewayAddress getByHostAndPort(String host, Integer port);
}
