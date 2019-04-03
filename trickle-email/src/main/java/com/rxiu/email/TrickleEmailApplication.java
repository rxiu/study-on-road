package com.rxiu.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.rxiu.email.core",
        "com.rxiu.email.service"
})
public class TrickleEmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrickleEmailApplication.class, args);
    }

}
