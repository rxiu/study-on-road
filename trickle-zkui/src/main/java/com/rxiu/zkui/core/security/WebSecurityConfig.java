package com.rxiu.zkui.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by rxiu on 2018/3/19.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true) // 控制权限注解
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("rxiu").password(new BCryptPasswordEncoder().encode("123456")).roles(Role.USER);
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("admin").password(new BCryptPasswordEncoder().encode("123456")).roles(Role.USER, Role.ADMIN);
        auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder()).withUser("braska").password(new BCryptPasswordEncoder().encode("123456")).roles(Role.USER);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
                .and().authorizeRequests()
                //.accessDecisionManager(new CustomAccessDecisionManager())
                .antMatchers("/login", "/login/**").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/images/**", "/js/**", "/css/**", "/fonts/**", "/favicon.ico").permitAll()
                .and().formLogin().loginPage("/login").successHandler(new CustomSuccessHandler())
                .usernameParameter("username").passwordParameter("password")
                .and().headers().frameOptions().disable()
                .and().csrf().disable();
    }
}
