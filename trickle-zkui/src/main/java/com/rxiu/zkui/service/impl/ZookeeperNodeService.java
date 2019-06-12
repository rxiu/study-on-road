package com.rxiu.zkui.service.impl;

import com.google.common.collect.Lists;
import com.rxiu.zkui.core.curator.ZkCurator;
import com.rxiu.zkui.core.curator.ZkCuratorManager;
import com.rxiu.zkui.core.exception.BasicException;
import com.rxiu.zkui.core.exception.ExceptionResult;
import com.rxiu.zkui.domain.ZkNode;
import com.rxiu.zkui.service.IZookeeperNodeService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shenyuhang
 * @date 2019/6/10
 **/
@Service
public class ZookeeperNodeService implements IZookeeperNodeService {

    @Override
    public List<ZkNode> getNodeChildren(String zkServer, String nodePath) {
        ZkCurator curator = ZkCuratorManager.getCurator(zkServer);
        return (List)curator.operater(() -> {
            try {
                return getNodeChildren(curator, nodePath, null);
            } catch (Exception e) {
                return new BasicException(ExceptionResult.CHECK_EXCEPTION, e.getMessage());
            } finally {
                ZkCuratorManager.release(zkServer, curator);
            }
        });
    }

    private static List<ZkNode> getNodeChildren(ZkCurator curator, String nodePath, ZkNode parent) {
        try {
            List<String> children = curator.getChildren(nodePath);
            if (children == null || children.isEmpty()) {
                return null;
            }

            List<ZkNode> list = Lists.newArrayListWithExpectedSize(16);
            for (String child : children) {
                if (ZkCuratorManager.ZK_SYSTEM_NODE.equals(child)) continue;

                String parentPath = parent == null ? "" : parent.getPath();
                String path = (parentPath + "/" + child);
                path = path.startsWith("/") ? path.substring(1) : path;
                ZkNode node = new ZkNode();
                node.setName(child);
                node.setPath(path);
                node.setChildren(getNodeChildren(curator, path, node));
                list.add(node);
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取节点信息
     * @return
     */
    public ZkNode getNodeInfo(String zkServer, String zkNode) {

        ZkCurator curator = ZkCuratorManager.getCurator(zkServer);
        return (ZkNode) curator.operater(() -> {
            ZkNode node = new ZkNode();
            node.setPath(zkNode);
            node.setName(zkNode.substring(zkNode.lastIndexOf("/") + 1));
            try {
                node.setValue(curator.get(zkNode));
            } catch (Exception e) {
                throw new BasicException(ExceptionResult.CHECK_EXCEPTION, e.getMessage());
            } finally {
                ZkCuratorManager.release(zkServer, curator);
            }
            return node;
        });
    }
}
