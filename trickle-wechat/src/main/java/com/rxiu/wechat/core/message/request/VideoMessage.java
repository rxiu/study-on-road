package com.rxiu.wechat.core.message.request;

/**
 * @author rxiu
 * @date 2018/07/19.
 **/
public class VideoMessage extends RequestMessage {
    private String MediaId;
    private String ThumbMediaId;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getThumbMediaId() {
        return ThumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        ThumbMediaId = thumbMediaId;
    }
}
