package com.rxiu.wechat.message.response;

import com.rxiu.wechat.message.Message;

/**
 * @author rxiu
 * @date 2018/07/19.
 **/
public class ImageMessage extends Message {

    private Image Image;

    public static class Image {

        private String MediaId;

        public String getMediaId() {
            return MediaId;
        }

        public void setMediaId(String mediaId) {
            MediaId = mediaId;
        }

    }
}
