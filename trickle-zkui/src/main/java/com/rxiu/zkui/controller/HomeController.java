package com.rxiu.zkui.controller;

import com.google.common.base.Strings;
import com.rxiu.zkui.core.PropertyPlaceHolder;
import com.rxiu.zkui.core.ZkCuratorBuilder;
import com.rxiu.zkui.core.security.Role;
import com.rxiu.zkui.domain.ZkNode;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author shenyuhang
 * @date 2019/4/10
 */
@Controller
public class HomeController {

    @GetMapping(value = {"/", "/home"})
    @Secured(value = {"ROLE_" + Role.ADMIN, "ROLE_" + Role.USER})
    public String homePage(ModelMap model) {
        model.addAttribute("parentPath", "/");
        model.addAttribute("user", getPrincipal());
        return "home";
    }

    @GetMapping("/main")
    @Secured(value = {"ROLE_" + Role.ADMIN, "ROLE_" + Role.USER})
    public String main() {
        return "main";
    }

    private String getPrincipal() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }


    public Object getNode(String zkPath) {
        return null;
    }
}
