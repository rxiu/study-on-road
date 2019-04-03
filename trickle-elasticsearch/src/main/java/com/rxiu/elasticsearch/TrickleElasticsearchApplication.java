package com.rxiu.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.rxiu.elasticsearch.core",
        "com.rxiu.elasticsearch.service"
})
public class TrickleElasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrickleElasticsearchApplication.class, args);
    }

}
