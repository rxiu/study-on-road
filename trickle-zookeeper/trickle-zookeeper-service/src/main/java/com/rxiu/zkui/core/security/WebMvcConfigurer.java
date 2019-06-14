package com.rxiu.zkui.core.security;

import com.rxiu.zkui.core.PropertyPlaceHolder;
import freemarker.template.TemplateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rxiu on 2018/3/20.
 */
@Configuration
//@EnableWebMvc
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    private static final String charset = "UTF-8";
    final List<Resource> resourceList = new ArrayList<Resource>();

    @Bean
    public PropertyPlaceHolder property() {
        PropertyPlaceHolder placeHolder = new PropertyPlaceHolder();
        Resource resource = new ClassPathResource("application.properties");
        resourceList.add(resource);
        placeHolder.setLocations(resourceList.toArray(new Resource[]{}));
        return placeHolder;
    }

    /**
     * 全局配置跨域
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .maxAge(3600)
                .allowCredentials(true);
    }

    @Bean
    public freemarker.template.Configuration registerFreemarker() {
        freemarker.template.Configuration configuration = new freemarker.template.Configuration();
        configuration.setDateFormat("yyyy/MM/dd");
        configuration.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");

        try {
            configuration.setSetting("template_update_delay", "1");
            configuration.setSetting("default_encoding", charset);
        } catch (TemplateException e) {
            e.printStackTrace();
        }

        return configuration;
    }

    @Bean
    public FreeMarkerViewResolver registerViewResolver() {
        FreeMarkerViewResolver viewResolver = new FreeMarkerViewResolver();
        viewResolver.setContentType(String.format("text/html;charset=%s", charset));
        viewResolver.setPrefix("/views/");
        viewResolver.setSuffix(".html");
        viewResolver.setOrder(0);
        viewResolver.setCache(false);
        return viewResolver;
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}