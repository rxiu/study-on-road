package com.rxiu.zkui.core;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.rxiu.zkui.domain.ZkNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author shenyuhang
 * @date 2019/4/10
 */
public class ZkCuratorBuilder {

    private static final Logger logger = LoggerFactory.getLogger(ZkCuratorBuilder.class);
    public final static String ZK_SYSTEM_NODE = "zookeeper";

    private static String zkHosts;
    private static String zkNode;
    private static Integer retry;

    private static ZkCurator curator;

    private static class SINGLETON {
        private static final ZkCuratorBuilder builder = new ZkCuratorBuilder();
    }

    public static ZkCuratorBuilder configure(String zkServer, String basePath) {
        zkHosts = Preconditions.checkNotNull(zkServer, "zkServer must be specify.");

        zkNode = Strings.isNullOrEmpty(basePath) ? "" : basePath;

        Integer connectTimeout = PropertyPlaceHolder.getInteger("zkConnectTimeout");

        Integer sessionTimeout = PropertyPlaceHolder.getInteger("zkSessionTimeout");

        retry = PropertyPlaceHolder.getInteger("zkMaxRetry");

        curator = new ZkCurator(zkHosts, zkNode, connectTimeout, sessionTimeout, retry);

        return SINGLETON.builder;
    }

    public static List<ZkNode> getChildren() {
        curator.start();
        try {
            return getChildren(zkNode, null);
        } catch (Exception e) {
            logger.error("failed recursive zookeeper node: ｛｝", e);
            return null;
        } finally {
            curator.close();
        }
    }

    /**
     * 递归获取所有节点
     * @param nodePath
     * @return
     */
    public static List<ZkNode> getChildren(String nodePath) {
        curator.start();
        try {
            List<String> children = curator.getChildren(nodePath);
            if (children == null || children.isEmpty()) return null;

            List<ZkNode> list = Lists.newArrayListWithExpectedSize(16);
            for (String child : children) {
                ZkNode node = new ZkNode();

                String parentPath = "/".equals(nodePath) ? "" : nodePath;
                String path = (parentPath + "/" + child);
                path = path.startsWith("/") ? path.substring(1) : path;

                node.setPath(path);
                node.setName(child);

                node.setIsParent(curator.hasChild(path));
                list.add(node);
            }
            return list;
        } catch (Exception e) {
            logger.error("error to get zk nodes: {}", e);
            return null;
        } finally {
            curator.close();
        }
    }

    /**
     * 获取子节点
     * @param nodePath
     * @param parent
     * @return
     */
    private static List<ZkNode> getChildren(String nodePath, ZkNode parent) {
        try {
            List<String> children = curator.getChildren(nodePath);
            if (children == null || children.isEmpty()) {
                return null;
            }

            List<ZkNode> list = Lists.newArrayListWithExpectedSize(16);
            for (String child : children) {
                if (ZK_SYSTEM_NODE.equals(child)) continue;

                String parentPath = parent == null ? "" : parent.getPath();
                String path = (parentPath + "/" + child);
                path = path.startsWith("/") ? path.substring(1) : path;
                ZkNode node = new ZkNode();
                node.setName(child);
                node.setPath(path);
                node.setChildren(getChildren(path, node));
                list.add(node);
            }
            return list;
        } catch (Exception e) {
            logger.error("failed recursive zookeeper node: ｛｝", e);
            return null;
        }
    }
}
