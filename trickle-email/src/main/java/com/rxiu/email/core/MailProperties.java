package com.rxiu.email.core;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by rxiu on 2018/3/13.
 */
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {

    public static class Smtp {
        private String auth;
        private String timeout;
        private boolean useSSL;

        public void setAuth(String auth) {
            this.auth = auth;
        }

        public String getTimeout() {
            return timeout;
        }

        public void setTimeout(String timeout) {
            this.timeout = timeout;
        }

        public String getAuth() {
            return auth;
        }

        public boolean isUseSSL() {
            return useSSL;
        }

        public void setUseSSL(boolean useSSL) {
            this.useSSL = useSSL;
        }
    }

    String host;
    Integer port;
    String username;
    String password;
    String protocol;
    String charset;
    Smtp smtp;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Smtp getSmtp() {
        return smtp;
    }

    public void setSmtp(Smtp smtp) {
        this.smtp = smtp;
    }
}
