package com.rxiu.security.core.config;

import freemarker.template.TemplateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * Created by rxiu on 2018/3/20.
 */
@Configuration
@EnableWebMvc
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    private static final String charset = "UTF-8";

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

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/error").setViewName("error");
        registry.addViewController("/admin").setViewName("admin");
        registry.addViewController("/more").setViewName("more");
        registry.addViewController("/index").setViewName("index");
    }

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/views/");
        viewResolver.setSuffix(".jsp");
        viewResolver.setOrder(1);
        return viewResolver;
    }

    @Bean
    public FreemarkerConfiguration registerFreemarker() {
        FreemarkerConfiguration configuration = new FreemarkerConfiguration();
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

    class FreemarkerConfiguration extends freemarker.template.Configuration{}
}