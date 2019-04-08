package com.rxiu.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.rxiu.wechat.common.util.HttpUtil;
import com.rxiu.wechat.core.PropertyPlaceHolder;
import com.rxiu.wechat.core.compent.Menu;
import com.rxiu.wechat.core.storage.Storage;
import com.rxiu.wechat.service.IMenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

/**
 * @author rxiu
 * @date 2018/07/20.
 **/
@Service
public class MenuServiceImpl implements IMenuService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);
    private static String menuCreateUrl = PropertyPlaceHolder.getString("wechat.menu.create.url");

    @Autowired
    Storage storage;

    @Override
    public int createMenu() {

        String json = "";
        InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream("toolbar.config");
        try {
            if (stream != null && stream.available() > 0) {
                byte[] bytes = new byte[stream.available()];
                stream.read(bytes);
                json = new String(bytes, Charsets.UTF_8);
            }

            if(!Strings.isNullOrEmpty(json)) {
                try {
                    JSONObject.parseObject(json);
                } catch (RuntimeException e) {
                    throw new RuntimeException("微信菜单配置有误", e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("微信菜单配置文件读取失败",e);
        }
        return createMenu(json);
    }

    public int createMenu(Object menu) {
        String json = "";
        if (menu instanceof Menu) {
            json = JSONObject.toJSONString(menu);
        }
        if (menu instanceof String) {
            json = menu.toString();
        }

        String accessToken = storage.getAccessToken();

        String url = HttpUtil.buildUrl(menuCreateUrl, Collections.singletonMap("access_token", accessToken));
        String result = HttpUtil.sendPostBuffer(url, json);
        LOGGER.info("创建微信菜单: {}", result);
        return Strings.isNullOrEmpty(result) ? Integer.MIN_VALUE : JSONObject.parseObject(result).getInteger("errcode");
    }
}
