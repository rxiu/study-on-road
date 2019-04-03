package com.rxiu.security.core.config;

import com.rxiu.security.common.util.MD5Util;
import com.rxiu.security.core.BeanFactory;
import com.rxiu.security.core.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/**
 * Created by rxiu on 2018/3/19.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    SecurityService securityService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/login").permitAll()
            .antMatchers("/login/**").permitAll()
            .antMatchers("/logout").permitAll()
            .antMatchers("/error").permitAll()
            .antMatchers("/images/**").permitAll()
            .antMatchers("/js/**").permitAll()
            .antMatchers("/css/**").permitAll()
            .antMatchers("/fonts/**").permitAll()
            .antMatchers("/favicon.ico").permitAll()
            .antMatchers("/").permitAll()
            .antMatchers("/kaptcha/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login")
            .usernameParameter("username").passwordParameter("password")
            .successHandler(new CustomAuthSuccessHandler())
            .failureHandler(new CustomAuthFailtureHandler())
            .and()
            .sessionManagement().maximumSessions(1).sessionRegistry(getSessionRegistry())
            .and().and()
            .logout()
            .logoutSuccessHandler(new CustomLogoutSuccessHandler())
            .logoutUrl("/logout")
            .invalidateHttpSession(true).clearAuthentication(true);

        http.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler());
        http.addFilterBefore(BeanFactory.getBean(CustomFilterSecurityInterceptor.class), FilterSecurityInterceptor.class).csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(getAuthenticationProvider());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return MD5Util.encode((String) rawPassword);
            }
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return encodedPassword.equals(MD5Util.encode((String) rawPassword));
            }
        };
    }

    private SessionRegistry getSessionRegistry(){
        SessionRegistry sessionRegistry=new SessionRegistryImpl();
        return sessionRegistry;
    }

    @Bean
    public DaoAuthenticationProvider getAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(securityService);
        provider.setHideUserNotFoundExceptions(false);
        provider.setPasswordEncoder(getPasswordEncoder());
        return provider;
    }
}
