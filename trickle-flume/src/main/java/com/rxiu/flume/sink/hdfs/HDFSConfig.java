package com.rxiu.flume.sink.hdfs;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;

/**
 * Created by shenyuhang on 2017/9/22.
 */
public class HDFSConfig {

    private boolean isHaEnv;
    private String nameNodeServer;
    private List<Server> servers;

    public boolean isHaEnv() {
        return isHaEnv;
    }

    public void setHaEnv(boolean haEnv) {
        isHaEnv = haEnv;
    }

    public String getNameNodeServer() {
        return nameNodeServer;
    }

    public void setNameNodeServer(String nameNodeServer) {
        this.nameNodeServer = nameNodeServer;
    }

    public void setServers(String servers) {
        this.servers = loadServers(servers);
    }

    public List<Server> getServers() {
        return this.servers;
    }

    private List loadServers(String serverInfo) {
        List<Map> lists = JSONObject.parseObject(serverInfo, List.class);
        List servers = Lists.newArrayList();
        lists.forEach(server -> {
            String json = JSONObject.toJSONString(server.get("server"));
            servers.add(JSONObject.parseObject(json, Server.class));
        });
        return servers;
    }

    public static class Server {
        private String name;
        private String host;
        private Integer port;

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
    }

}
