package com.rxiu.zkui.controller;

import com.google.common.collect.Lists;
import com.rxiu.zkui.common.PageList;
import com.rxiu.zkui.common.ResponseResult;
import com.rxiu.zkui.core.security.Role;
import com.rxiu.zkui.domain.ZkCluster;
import com.rxiu.zkui.service.IZookeeperClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rxiu
 * @date 2019/4/12
 */
@Controller
@RequestMapping("cluster")
public class ZookeeperClusterController {

    @Autowired
    private IZookeeperClusterService service;

    @GetMapping("/index")
    @Secured(value = {"ROLE_" + Role.ADMIN, "ROLE_" + Role.USER})
    public String index() {
        return "cluster/index";
    }


    @GetMapping
    @ResponseBody
    @Secured(value = {"ROLE_" + Role.ADMIN, "ROLE_" + Role.USER})
    public Object getCluster(ZkCluster cluster, Integer page, Integer rows) {
        page = page <= 0 ? 0 : page - 1;
        PageList<ZkCluster> pageList = service.getByPage(page, rows, new ZkCluster());
        return pageList;
    }

    @PostMapping
    @ResponseBody
    @Secured(value = {"ROLE_" + Role.ADMIN})
    public Object add(@RequestBody ZkCluster cluster) {
        return ResponseResult.success("添加成功", service.add(cluster));
    }

    @ResponseBody
    @GetMapping(value = "/{id}")
    @Secured(value = {"ROLE_" + Role.ADMIN, "ROLE_" + Role.USER})
    public Object getCluster(@PathVariable Long id) {
        return ResponseResult.success(service.getById(id));
    }

    @ResponseBody
    @PutMapping
    @Secured(value = {"ROLE_" + Role.ADMIN})
    public Object update(@RequestBody ZkCluster cluster) {
        return ResponseResult.success("更新成功", service.save(cluster));
    }

    @ResponseBody
    @DeleteMapping
    @Secured(value = {"ROLE_" + Role.ADMIN})
    public Object delete(String ids) {
        List<Long> idList =  Lists.newArrayList(ids.split(",")).parallelStream().map(Long::parseLong).collect(Collectors.toList());
        return ResponseResult.success("删除成功", service.deleteByIds(idList));
    }

    /**
     * todo 获取集群快照
     *
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("/info")
    @Secured(value = {"ROLE_" + Role.ADMIN, "ROLE_" + Role.USER})
    public Object getClusterInfo(String id) {
        return ResponseResult.success("获取集群快照成功", "集群快照信息啊啊啊啊啊啊");
    }
}
