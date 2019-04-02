package com.rxiu.security.web.model;

import java.io.Serializable;

/**
 * Created by rxiu on 2018/3/19.
 */
public class BaseEntity implements Serializable {

    private Integer id;
/*    private String createBy;
    private Date createAt;
    private String updateBy;
    private Date updateAt;
    private String status;*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
