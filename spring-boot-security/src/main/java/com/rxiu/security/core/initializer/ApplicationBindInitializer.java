package com.rxiu.security.core.initializer;

import com.rxiu.security.core.checker.ServerSerialChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 应用启动时检测是否绑定过服务器
 * @author rxiu
 * @date 2018/09/28.
 **/
public class ApplicationBindInitializer implements ApplicationContextInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationBindInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        try {
            ServerSerialChecker checker = ServerSerialChecker
                    .builder()
                    .configure();

            if (checker.verify()) {
                LOGGER.error("设备校验失败.");
                System.exit(0);
            }
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            System.exit(0);
        }
    }
}
