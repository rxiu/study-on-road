package com.rxiu.wechat.core.dispatcher;

import com.rxiu.wechat.common.Constant;
import com.rxiu.wechat.common.util.SmartChatUtil;
import com.rxiu.wechat.common.util.WeChatUtil;
import com.rxiu.wechat.core.message.MessageType;
import com.rxiu.wechat.core.message.response.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author rxiu
 * @date 2018/07/19.
 **/
public class MessageDispatcher extends AbstractDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageDispatcher.class);

    public String doProcess(Map<String, String> map, String from, String to) {
        String msgType = map.get(Constant.WECHAT_MSG_TYPE);
        LOGGER.info("消息类型：{}", msgType);

        if (MessageType.Request.text.name().equals(msgType)) {
            TextMessage message = new TextMessage(to, from, SmartChatUtil.getReply(msgType));
            return WeChatUtil.MessageTool.messageToXml(message);
        }

        if (MessageType.Request.image.name().equals(msgType)) {
            return WeChatUtil.MessageTool.messageToXml(new TextMessage(to, from, "图片消息"));
        }

        if (MessageType.Request.link.name().equals(msgType)) {
        }

        if (MessageType.Request.location.name().equals(msgType)) {
            return WeChatUtil.MessageTool.messageToXml(new TextMessage(to, from, "定位消息"));
        }

        if (MessageType.Request.vedio.name().equals(msgType)) {
            return WeChatUtil.MessageTool.messageToXml(new TextMessage(to, from, "视频消息"));
        }

        if (MessageType.Request.voice.name().equals(msgType)) {
            return WeChatUtil.MessageTool.messageToXml(new TextMessage(to, from, "语音消息"));
        }

        if (MessageType.Request.shortvedio.name().equals(msgType)) {
            return WeChatUtil.MessageTool.messageToXml(new TextMessage(to, from, "短视频消息"));
        }

        return null;
    }

}
