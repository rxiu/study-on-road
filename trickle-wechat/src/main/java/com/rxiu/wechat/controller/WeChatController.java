package com.rxiu.wechat.controller;

import com.rxiu.wechat.common.util.WeChatUtil;
import com.rxiu.wechat.dispatcher.Dispatcher;
import com.rxiu.wechat.dispatcher.DispatcherBuilder;
import com.rxiu.wechat.service.IMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author rxiu
 * @date 2018/07/19.
 **/
@Controller
@RequestMapping("/wechat")
public class WeChatController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeChatController.class);

    @Autowired
    IMenuService menuService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public void doGet(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam(value = "signature") String signature,
                      @RequestParam(value = "timestamp") String timestamp,
                      @RequestParam(value = "nonce") String nonce,
                      @RequestParam(value = "echostr") String echostr) {
        try {
            if (WeChatUtil.SignatureTool.checkSignature(signature, timestamp, nonce)) {
                PrintWriter out = response.getWriter();
                out.write(echostr);
                out.flush();
                out.close();
            } else {
                LOGGER.info("invalid request.");
            }
        } catch (IOException e) {
            LOGGER.error("wechat signature check error.{}", e);
        }
    }

    @RequestMapping(value = "index", method = RequestMethod.POST)
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, String> map = WeChatUtil.MessageTool.parseXml(request);

            Dispatcher dispatcher = DispatcherBuilder.configure(map).build();

            response.setCharacterEncoding("utf-8");
            PrintWriter out = response.getWriter();
            out.write(dispatcher.process(map));
            out.flush();
            out.close();
        } catch (Exception e) {
            LOGGER.info("failed to parse request parameter.{}", e);
        }
    }

    @ResponseBody
    @RequestMapping(value = "menu/load", method = RequestMethod.POST)
    public Object loadMenu() {
        return menuService.createMenu();
    }

}
