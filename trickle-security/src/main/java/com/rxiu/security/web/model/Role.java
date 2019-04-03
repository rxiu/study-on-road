package com.rxiu.security.web.model;

import javax.persistence.Transient;
import java.util.List;

/**
 * Created by rxiu on 2018/3/19.
 */
public class Role extends BaseEntity {

    private String name;
    @Transient
    private List<Authority> authorities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }
}
