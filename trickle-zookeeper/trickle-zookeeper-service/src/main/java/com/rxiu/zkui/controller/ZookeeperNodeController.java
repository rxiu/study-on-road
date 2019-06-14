package com.rxiu.zkui.controller;

import com.google.common.base.Strings;
import com.rxiu.zkui.common.ResponseResult;
import com.rxiu.zkui.core.PropertyPlaceHolder;
import com.rxiu.zkui.core.security.Role;
import com.rxiu.zkui.domain.ZkNode;
import com.rxiu.zkui.service.IZookeeperNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author rxiu
 * @date 2019/4/12
 */
@Controller
@RequestMapping("node")
public class ZookeeperNodeController {

    @Autowired
    IZookeeperNodeService nodeService;

    @GetMapping
    @Secured(value = {"ROLE_" + Role.ADMIN, "ROLE_" + Role.USER})
    public String index() {
        return "node/index";
    }

    @ResponseBody
    @PostMapping("tree")
    @Secured(value = {"ROLE_" + Role.ADMIN, "ROLE_" + Role.USER})
    public Object home(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

        String zkServer = PropertyPlaceHolder.getString("zkServer");

        String zkPath = Strings.isNullOrEmpty(request.getParameter("path")) ? "" : request.getParameter("path");
        String navigate = request.getParameter("navigate");
        List<ZkNode> nodes = nodeService.getNodeChildren(zkServer, zkPath);
        return ResponseResult.success("节点列表获取成功", nodes);
    }

    @ResponseBody
    @PostMapping("info")
    @Secured(value = {"ROLE_" + Role.ADMIN, "ROLE_" + Role.USER})
    public Object node(HttpServletRequest request) {
        String zkNode = request.getParameter("zkNode");
        String zkServer = PropertyPlaceHolder.getString("zkServer");

        ZkNode node = nodeService.getNodeInfo(zkServer, zkNode);
        return ResponseResult.success("节点信息获取成功", node);
    }
}
