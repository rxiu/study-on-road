package com.rxiu.zkui.core.config;

import org.h2.Driver;
import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

/**
 * @author shenyuhang
 * @date 2019/6/11
 **/
@Configuration
public class H2Configuration {

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource ds = new SimpleDriverDataSource();
        ds.setDriverClass(Driver.class);
        ds.setUrl("jdbc:h2:file:~/test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false;FILE_LOCK=FS");
        ds.setUsername("root");
        ds.setPassword("123456");
        return ds;
    }

    @Bean
    public ServletRegistrationBean<WebServlet> h2Console() {
        String path = "/h2/console";
        boolean isTrace = false;
        boolean webAllow = true;
        String urlMapping = path + (path.endsWith("/") ? "*" : "/*");
        ServletRegistrationBean<WebServlet> registration = new ServletRegistrationBean<>(
                new WebServlet(), urlMapping);

        if (isTrace) {
            registration.addInitParameter("trace", "");
        }
        if (webAllow) {
            registration.addInitParameter("webAllowOthers", "");
        }
        return registration;
    }
}
