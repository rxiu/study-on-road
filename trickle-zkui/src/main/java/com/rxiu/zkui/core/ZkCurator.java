package com.rxiu.zkui.core;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * @author shenyuhang
 * @date 2019/4/10
 */
public class ZkCurator {

    private static final Logger logger = LoggerFactory.getLogger(ZkCurator.class);

    private final static Integer DEFAULT_SESSION_TIMEOUT = 50000;
    private final static Integer DEFAULT_CONNECT_TIMEOUT = 10000;
    private final static Integer DEFAULT_BASE_SLEEP_TIME = 1000;
    private final static Integer DEFAULT_MAX_RETRY = 5;
    private final static Charset charset = Charsets.UTF_8;
    private final static String NAMESPACE = "";

    private String zkHosts;
    private String basePath;
    private Integer connectTimeout;
    private Integer sessionTimeout;
    private Integer maxRetry;
    private CuratorFramework client;
    private final Map<String, TreeCache> treeCaches = Maps.newHashMap();

    public ZkCurator(String zkHosts, String basePath) {
        this(zkHosts, basePath, DEFAULT_CONNECT_TIMEOUT);
    }

    public ZkCurator(String zkHosts, String basePath, Integer connectTimeout) {
        this(zkHosts, basePath, connectTimeout, DEFAULT_SESSION_TIMEOUT);
    }

    public ZkCurator(String zkHosts, String basePath, Integer connectTimeout, Integer sessionTimeout) {
        this(zkHosts, basePath, connectTimeout, sessionTimeout, DEFAULT_MAX_RETRY);
    }

    public ZkCurator(String zkHosts, String basePath, Integer connectTimeout, Integer sessionTimeout, Integer maxRetry) {
        this.zkHosts = zkHosts;
        this.basePath = basePath;
        this.connectTimeout = connectTimeout == null ? DEFAULT_CONNECT_TIMEOUT : connectTimeout;
        this.sessionTimeout = sessionTimeout == null ? DEFAULT_SESSION_TIMEOUT : sessionTimeout;
        this.maxRetry = maxRetry == null ? DEFAULT_MAX_RETRY : maxRetry;

        client = CuratorFrameworkFactory.builder()
                .connectString(zkHosts)
                .retryPolicy(new ExponentialBackoffRetry(DEFAULT_BASE_SLEEP_TIME, this.maxRetry))
                .sessionTimeoutMs(this.sessionTimeout)
                .connectionTimeoutMs(this.connectTimeout)
                .namespace(NAMESPACE)
                .build();
    }

    public CuratorFramework start() {
        if (!isStarted()) {
            logger.info("zk client starting.");
            client.start();
        }
        return client;
    }

    public boolean isStarted() {
        return client != null && client.getState() == CuratorFrameworkState.STARTED;
    }

    public void addListener(String node, TreeCacheListener listener) throws Exception {
        logger.info("add zk listener..");
        if(treeCaches.containsKey(node)) return;

        node = fixed(node);
        TreeCache treeCache = new TreeCache(this.client, node);
        treeCache.getListenable().addListener(listener);
        treeCache.start();
        treeCaches.put(node, treeCache);
    }

    public Stat checkExists(String nodePath) {
        try {
            return client.checkExists().forPath(nodePath);
        } catch (Exception e) {
            logger.error("zk check node exists error: {}", e);
            return null;
        }
    }

    public String send(String nodePath, String content) throws Exception {
        return send(nodePath, Strings.nullToEmpty(content).getBytes(charset));
    }

    public String send(String nodePath, byte[] bytes) throws Exception {
        nodePath = fixed(nodePath);
        Stat stat = checkExists(nodePath);
        if (stat == null) {
            client.create().creatingParentContainersIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(nodePath, bytes);
        } else {
            client.setData().forPath(nodePath, bytes);
        }
        return nodePath;
    }

    public String get(String nodePath) throws Exception {
        nodePath = fixed(nodePath);
        Stat stat = checkExists(nodePath);
        if (stat != null) {
            byte[] bytes = client.getData().forPath(nodePath);
            if (bytes != null) {
                return new String(bytes, charset);
            }
        }
        return null;
    }

    public String delete(String nodePath) throws Exception {
        nodePath = fixed(nodePath);

        Stat stat = checkExists(nodePath);
        if (stat != null)
            client.delete().deletingChildrenIfNeeded().forPath(nodePath);

        return nodePath;
    }

    public void close() {
        client.close();
        client = null;
    }

    private static String fixed(String path) {
        path = path.startsWith("/") ? path : String.format("/%s", path);
        return path.replaceAll("[/]{2,}", "/");
    }

    public String buildPath(String... zNodeParts) {
        StringBuilder builder = new StringBuilder(256);

        String path;
        if (zNodeParts != null && zNodeParts.length != 0) {
            builder.append("/");

            path = Joiner.on("/").appendTo(builder, zNodeParts).toString();
        } else {
            path = builder.toString();
        }

        builder = null;
        return fixed(path);
    }

    public List<String> getChildren(String nodePath){
        nodePath = fixed(nodePath);
        Stat stat = checkExists(nodePath);
        if (stat != null) {
            try {
                return client.getChildren().forPath(nodePath);
            } catch (Exception e) {
                logger.error("fail to get zk node child: []", e);
                return null;
            }
        }
        return null;
    }

    public boolean hasChild(String nodePath) {
        List<String> children = getChildren(nodePath);
        return children != null && children.size() > 0;
    }
}
