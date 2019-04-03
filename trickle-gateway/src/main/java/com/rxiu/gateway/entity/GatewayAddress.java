package com.rxiu.gateway.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public class GatewayAddress extends BaseEntity implements Serializable {

    public enum StrategyType {
        ROUND("0"),
        RANDOM("1"),
        HASH("2");
        private String code;

        StrategyType(String code) {
            this.code = code;
        }

        public static StrategyType of(String code) {
            for (StrategyType type : StrategyType.values()) {
                if (code.equals(type.code)) {
                    return type;
                }
            }
            return null;
        }
    }

    private String id;
    private String fkStrategyId;
    private String name;
    private String host;
    private Integer port;
    private Integer cachePort;
    private Integer state;
    private String createBy;
    private Date createAt;
    private String modifyBy;
    private Date modifyAt;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public String getFkStrategyId() {
        return fkStrategyId;
    }

    public void setFkStrategyId(String fkStrategyId) {
        this.fkStrategyId = fkStrategyId;
    }

    public Integer getCachePort() {
        return cachePort;
    }

    public void setCachePort(Integer cachePort) {
        this.cachePort = cachePort;
    }
}
