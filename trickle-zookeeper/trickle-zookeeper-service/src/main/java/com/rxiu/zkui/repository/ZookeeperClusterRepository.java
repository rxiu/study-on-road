package com.rxiu.zkui.repository;

import com.rxiu.zkui.domain.ZkCluster;

/**
 * @author shenyuhang
 * @date 2019/6/11
 **/
public interface ZookeeperClusterRepository extends BaseRepository<ZkCluster> {

    ZkCluster findByHostList(String hostList);
}
