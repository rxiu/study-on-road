package com.rxiu.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@EnableZuulProxy
@ComponentScan(basePackages = {
        "com.rxiu.gateway.core",
        "com.rxiu.gateway.service"
})
public class TrickleGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrickleGatewayApplication.class, args);
    }

}
