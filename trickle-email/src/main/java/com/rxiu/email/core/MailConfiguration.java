package com.rxiu.email.core;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * Created by shenyuhang on 2018/3/13.
 */
@Configuration
public class MailConfiguration {

    @Autowired
    MailProperties prop;

    @Bean
    JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(prop.getHost());
        javaMailSender.setPort(prop.getPort());
        javaMailSender.setUsername(prop.getUsername());
        javaMailSender.setPassword(prop.getPassword());
        javaMailSender.setJavaMailProperties(getMailProperties());
        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", prop.getSmtp().getAuth());
        properties.setProperty("mail.smtp.timeout", prop.getSmtp().getTimeout());

        if(prop.getSmtp().isUseSSL()){
            MailSSLSocketFactory sf = null;
            try {
                sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
                properties.put("mail.smtp.ssl.enable", "true");
                properties.put("mail.smtp.ssl.socketFactory", sf);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
        }

        return properties;
    }
}
