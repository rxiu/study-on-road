package com.rxiu.wechat.core.message.request;

/**
 * @author rxiu
 * @date 2018/07/19.
 **/
public class ImageMessage extends RequestMessage {
    private String PicUrl;

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }
}
