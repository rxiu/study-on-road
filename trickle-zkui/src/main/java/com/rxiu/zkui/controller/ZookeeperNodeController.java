package com.rxiu.zkui.controller;

import com.google.common.base.Strings;
import com.rxiu.zkui.core.PropertyPlaceHolder;
import com.rxiu.zkui.core.ZkCuratorBuilder;
import com.rxiu.zkui.core.security.Role;
import com.rxiu.zkui.domain.ZkNode;
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
 * @author shenyuhang
 * @date 2019/4/12
 */
@Controller
@RequestMapping("node")
public class ZookeeperNodeController {


    @GetMapping
    @Secured(value = {"ROLE_" + Role.ADMIN, "ROLE_" + Role.USER})
    public String index() {
        return "node/index";
    }

    @ResponseBody
    @PostMapping
    @Secured(value = {"ROLE_" + Role.ADMIN, "ROLE_" + Role.USER})
    public Object home(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

        String zkServer = PropertyPlaceHolder.getString("zkServer");

        String zkPath = Strings.isNullOrEmpty(request.getParameter("path")) ? "" : request.getParameter("path");
        String navigate = request.getParameter("navigate");
        List<ZkNode> nodes = ZkCuratorBuilder.configure(zkServer, zkPath).getChildren();
        return nodes;
    }
}
