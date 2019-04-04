package com.rxiu.wechat.common.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.rxiu.wechat.core.PropertyPlaceHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author rxiu
 * @date 2018/07/20.
 **/
public class SmartChatUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmartChatUtil.class);

    public static final String url = PropertyPlaceHolder.getString("wechat.smart.chat.url");

    public static String getReply(String message) {
        LOGGER.info("智能聊天发送对话：{}", message);
        Map<String, String> params = Maps.newHashMapWithExpectedSize(2);
        params.put("key", PropertyPlaceHolder.getString("wechat.smart.chat.apikey"));
        params.put("info", message);

        String result = HttpUtil.sendPost(url, params);
        LOGGER.info("智能聊天收到回复：{}", result);
        if (result != null) {
            String reply = JSONObject.parseObject(result).getString("text");
            return Strings.isNullOrEmpty(reply) ? "听不懂你在说啥" : reply;
        }
        return "听不懂你在说啥";
    }

    public enum ReplyType {
        errKey(40001),
        errInfo(40002),
        errLimit(40004),
        errFormat(40007),

        text(100000),
        link(200000),
        news(302000),
        order(308000),
        music(313000),
        poem(314000);
        Integer index;

        ReplyType(Integer index) {
            this.index = index;
        }
    }
}
