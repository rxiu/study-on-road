package com.rxiu.zkui.core.security;

/**
 * @author rxiu
 * @date 2019/4/12
 */
public class Role {

    public static final String ADMIN = "ADMIN";

    public static final String USER = "USER";

    public final static String getRole(String role) {
        return "ROLE_" + role;
    }
}
