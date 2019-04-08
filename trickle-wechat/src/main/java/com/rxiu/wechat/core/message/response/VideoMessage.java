package com.rxiu.wechat.core.message.response;

import com.rxiu.wechat.core.message.Message;

/**
 * @author rxiu
 * @date 2018/07/19.
 **/
public class VideoMessage extends Message {

    private Video Video;

    public Video getVideo() {
        return Video;
    }

    public void setVideo(Video video) {
        Video = video;
    }

    public static class Video {

        private String MediaId;
        private String Title;
        private String Description;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getMediaId() {
            return MediaId;
        }

        public void setMediaId(String mediaId) {
            MediaId = mediaId;
        }

    }
}
