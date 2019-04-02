package com.rxiu.security.web.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by rxiu on 2018/3/19.
 */
public class Authority extends BaseEntity implements GrantedAuthority {

    public enum TYPE {
        DIR(1),
        MENU(2),
        OPERATE(3);
        Integer index;
        TYPE(Integer index) {
            this.index = index;
        }

        public static TYPE forValue(String index) {
            for (TYPE t : values()) {
                if (String.valueOf(t.index).equals(index)) {
                    return t;
                }
            }
            return null;
        }
    }

    private String name;
    private String url;
    private String method;
    private String type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getAuthority() {
        return String.join(";", url, method);
    }
}
