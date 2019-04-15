package com.rxiu.zkui.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author rxiu
 * @date 2018/07/20.
 **/
public class PropertyPlaceHolder extends PropertyPlaceholderConfigurer {

    private static Map<String, Object> ctxPropertiesMap;

    @Override
    protected void processProperties( ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess, props);
        ctxPropertiesMap = new HashMap<>();

        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            ctxPropertiesMap.put(keyStr, value);
        }
    }

    public static Object get(String name) {
        return ctxPropertiesMap.get(name);
    }

    public static Object getOrDefault(String name, Object defaultValue) {
        return ctxPropertiesMap.getOrDefault(name, defaultValue);
    }

    public static String getString(String name) {
        return ctxPropertiesMap.containsKey(name) ? (String)ctxPropertiesMap.get(name) : null;
    }

    public static Integer getInteger(String name) {
        return ctxPropertiesMap.containsKey(name) ? Integer.parseInt(getString(name)) : null;
    }
}
