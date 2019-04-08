package com.rxiu.wechat.core.configure;

import com.rxiu.wechat.core.PropertyPlaceHolder;
import com.rxiu.wechat.core.storage.MemoryStorage;
import com.rxiu.wechat.core.storage.Storage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rxiu
 * @date 2018/07/20.
 **/
@Configuration
public class SpringConfigure {
    final List<Resource> resourceList = new ArrayList<>();

    @Bean
    public PropertyPlaceHolder property() {
        PropertyPlaceHolder placeHolder = new PropertyPlaceHolder();
        Resource resource = new ClassPathResource("application.properties");
        resourceList.add(resource);
        placeHolder.setLocations(resourceList.toArray(new Resource[]{}));
        return placeHolder;
    }

    @Bean
    public Storage storage() {
        return new MemoryStorage();
    }
}
