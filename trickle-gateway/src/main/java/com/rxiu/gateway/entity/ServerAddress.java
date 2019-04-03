package com.rxiu.gateway.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public class ServerAddress extends BaseEntity implements Serializable {
    private String id;
    private String fkEsbGroupId;
    private String name;
    private String host;
    private Integer port;
    private Integer state;
    private String createBy;
    private Date createAt;
    private String modifyBy;
    private Date modifyAt;
    private Integer weight;

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    public String getFkEsbGroupId() {
        return fkEsbGroupId;
    }

    public void setFkEsbGroupId(String fkEsbGroupId) {
        this.fkEsbGroupId = fkEsbGroupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
