package com.rxiu.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import com.rxiu.wechat.common.util.WeChatUtil;
import com.rxiu.wechat.compent.Menu;
import com.rxiu.wechat.compent.button.Button;
import com.rxiu.wechat.compent.button.ClickButton;
import com.rxiu.wechat.compent.button.ViewButton;
import com.rxiu.wechat.service.IMenuService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author rxiu
 * @date 2018/07/20.
 **/
@Service
public class MenuServiceImpl implements IMenuService {

    public Menu menuInstance(Menu menu) {
        //创建点击一级菜单
        ClickButton button11 = new ClickButton();
        button11.setName("点击按钮");
        button11.setKey("11");
        button11.setType(Button.ButtonType.click.name());

        //创建跳转型一级菜单
        ViewButton button21 = new ViewButton();
        button21.setName("外联平台");
        button21.setType(Button.ButtonType.view.name());
        button21.setUrl("http://dashboard.tunnel.echomod.cn/wechat/auth");

        //创建其他类型的菜单与click型用法一致
        ClickButton button31 = new ClickButton();
        button31.setName("拍照发图");
        button31.setType(Button.ButtonType.pic_photo_or_album.name());
        button31.setKey("31");

        ClickButton button32 = new ClickButton();
        button32.setName("发送位置");
        button32.setKey("32");
        button32.setType(Button.ButtonType.location_select.name());

        //封装到一级菜单
        Button button = new Button();
        button.setName("一级菜单");
        button.setType(Button.ButtonType.click.name());
        button.setSub_button(new Button[]{button31,button32});

        //封装菜单
        menu.setButton(new Button[]{button11,button21,button});
        return menu;

    }

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
        return WeChatUtil.MenuTool.createMenu(json);
    }
}
