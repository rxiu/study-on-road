package com.rxiu.wechat.core.message.request;

/**
 * @author rxiu
 * @date 2018/07/19.
 **/
public class VoiceMessage extends RequestMessage {
    private String MediaId;
    private String Format;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getFormat() {
        return Format;
    }

    public void setFormat(String format) {
        Format = format;
    }
}
