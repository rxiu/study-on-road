package com.rxiu.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.rxiu.security.core",
        "com.rxiu.security.web.service",
        "com.rxiu.security.web.controller"
})
public class TrickleSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrickleSecurityApplication.class, args);
    }

}
