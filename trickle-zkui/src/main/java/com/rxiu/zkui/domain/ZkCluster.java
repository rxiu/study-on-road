package com.rxiu.zkui.domain;

/**
 * zookeeper 集群
 * @author shenyuhang
 * @date 2019/4/12
 */
public class ZkCluster {

    private String id;

    private String hostList;

    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostList() {
        return hostList;
    }

    public void setHostList(String hostList) {
        this.hostList = hostList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
