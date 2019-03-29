package com.rxiu.elasticsearch.core.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by rxiu on 2018/3/26.
 */
@Component
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchProperties {

    String clusterNodes;
    String clusterName;

    public String getClusterNodes() {
        return clusterNodes;
    }

    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
}
