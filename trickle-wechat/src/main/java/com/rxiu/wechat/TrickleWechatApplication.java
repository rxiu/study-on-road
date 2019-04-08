package com.rxiu.wechat;

import com.rxiu.wechat.common.util.WeChatUtil;
import com.rxiu.wechat.core.storage.MemoryStorage;
import com.rxiu.wechat.core.storage.RedisStorage;
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
        WeChatUtil.init(new MemoryStorage());
    }
}
