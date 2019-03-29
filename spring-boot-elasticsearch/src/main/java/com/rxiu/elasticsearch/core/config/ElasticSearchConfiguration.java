package com.rxiu.elasticsearch.core.config;

import com.rxiu.elasticsearch.core.prop.ElasticSearchProperties;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by shenyuhang on 2018/3/26.
 */
@Configuration
public class ElasticSearchConfiguration {

    @Autowired
    ElasticSearchProperties prop;

    @Bean
    public PreBuiltXPackTransportClient buildClient() throws UnknownHostException {
        PreBuiltXPackTransportClient client = new PreBuiltXPackTransportClient(settings());
        for (String clusterNode : prop.getClusterNodes().split(",")) {
            String hostName = clusterNode.substring(0, clusterNode.indexOf(":"));
            String port = clusterNode.substring(clusterNode.indexOf(":") + 1);
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostName), Integer.parseInt(port)));
        }
        client.connectedNodes();
        return client;
    }

    private Settings settings() {
        return Settings.builder()
                .put("cluster.name", prop.getClusterName())
                .put("client.transport.sniff", Boolean.TRUE)
                .put("xpack.security.transport.ssl.enabled", Boolean.FALSE)
                .put("xpack.security.user", "elastic:changeme")
                .put("client.transport.ignore_cluster_name", Boolean.FALSE)
                .put("client.transport.ping_timeout", "5s")
                .put("client.transport.nodes_sampler_interval", "5s")
                .build();
    }
}
