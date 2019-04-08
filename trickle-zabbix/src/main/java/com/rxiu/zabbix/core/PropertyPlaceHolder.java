package com.rxiu.zabbix.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author rxiu
 * @date 2019/4/8
 */
public class PropertyPlaceHolder {

    private static Map<String, Object> ctxPropertiesMap;

    public static void load(String source) {
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(source);

        Properties properties = new Properties();
        try {
            if (stream != null && stream.available() > 0) {
                properties.load(stream);
                ctxPropertiesMap = new HashMap<>();

                for (Object key : properties.keySet()) {
                    String keyStr = key.toString();
                    ctxPropertiesMap.put(keyStr, properties.getProperty(keyStr));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
