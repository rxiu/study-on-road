package com.rxiu.wechat.core.message.response;

import com.rxiu.wechat.core.message.Message;
import com.rxiu.wechat.core.message.MessageType;

/**
 * @author rxiu
 * @date 2018/07/19.
 **/
public class TextMessage extends Message {
    private String Content;

    public TextMessage(String from, String to, String content) {
        setFromUserName(from);
        setToUserName(to);
        setMsgType(MessageType.Response.text.name());
        setCreateTime(System.currentTimeMillis());
        this.Content = content;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
