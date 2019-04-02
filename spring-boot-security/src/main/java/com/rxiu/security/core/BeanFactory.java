package com.rxiu.security.core;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by rxiu on 2017/3/8.
 */
@Component
public class BeanFactory implements ApplicationContextAware {

    private static ApplicationContext SPRING_CONTEXT = null;

    private static ApplicationContext getSpringContext() {
        return SPRING_CONTEXT;
    }

    public static Object getBean(String name) {
        return getSpringContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getSpringContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getSpringContext().getBean(name, clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        if (BeanFactory.SPRING_CONTEXT == null) {
            BeanFactory.SPRING_CONTEXT = context;
        }
    }
}
