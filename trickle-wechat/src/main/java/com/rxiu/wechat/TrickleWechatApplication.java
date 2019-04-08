package com.rxiu.wechat;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrickleWechatApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(TrickleWechatApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    }
}
