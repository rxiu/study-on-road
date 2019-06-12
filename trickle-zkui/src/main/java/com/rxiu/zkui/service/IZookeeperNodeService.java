package com.rxiu.zkui.service;

import com.rxiu.zkui.domain.ZkNode;

import java.util.List;

/**
 * @author shenyuhang
 * @date 2019/6/10
 **/
public interface IZookeeperNodeService {

    /**
     * 获取子节点
     * @param zkServer
     * @param nodePath
     * @return
     */
    List<ZkNode> getNodeChildren(String zkServer, String nodePath);

    /**
     * 获取节点信息
     * @return
     */
    ZkNode getNodeInfo(String zkServer, String zkNode);
}
