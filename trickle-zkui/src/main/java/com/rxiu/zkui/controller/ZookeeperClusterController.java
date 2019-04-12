package com.rxiu.zkui.controller;

import com.google.common.collect.Lists;
import com.rxiu.zkui.core.PropertyPlaceHolder;
import com.rxiu.zkui.core.security.Role;
import com.rxiu.zkui.domain.PageList;
import com.rxiu.zkui.domain.ZkCluster;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author shenyuhang
 * @date 2019/4/12
 */
@Controller
@RequestMapping("cluster")
public class ZookeeperClusterController {

    @GetMapping
    @Secured(value = {"ROLE_" + Role.ADMIN, "ROLE_" + Role.USER})
    public String index() {
        return "cluster/index";
    }

    @PostMapping
    @ResponseBody
    @Secured(value = {"ROLE_" + Role.ADMIN, "ROLE_" + Role.USER})
    public Object getCluster() {

        /**
         * todo 从数据库获取集群列表
         */

        List<ZkCluster> clusters = Lists.newArrayListWithExpectedSize(32);
        ZkCluster cluster = new ZkCluster();
        cluster.setId("1");
        cluster.setCode(PropertyPlaceHolder.getString("zkServer"));
        cluster.setHostList(PropertyPlaceHolder.getString("zkServer"));
        clusters.add(cluster);

        PageList<ZkCluster> pageList = new PageList<>();
        pageList.setPage(1);
        pageList.setTotal(1);
        pageList.setRecords(1);
        pageList.setRows(clusters);
        return pageList;
    }

    @ResponseBody
    @PostMapping("/info")
    @Secured(value = {"ROLE_" + Role.ADMIN, "ROLE_" + Role.USER})
    public Object getClusterInfo(String id) {
        return "aaaaaaa";
    }
}
