package com.rxiu.wechat.core.message.request;

/**
 * @author rxiu
 * @date 2018/07/19.
 **/
public class TextMessage extends RequestMessage {
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
