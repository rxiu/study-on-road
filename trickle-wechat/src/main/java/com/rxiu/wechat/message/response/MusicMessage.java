package com.rxiu.wechat.message.response;

import com.rxiu.wechat.message.Message;

/**
 * @author rxiu
 * @date 2018/07/19.
 **/
public class MusicMessage extends Message {

    private Music Music;

    public Music getMusic() {
        return Music;
    }

    public void setMusic(Music music) {
        Music = music;
    }

    public static  class Music {
        private String Title;
        private String Description;
        private String MusicUrl;
        private String HQMusicUrl;

        private String ThumbMediaId;

        public String getThumbMediaId() {
            return ThumbMediaId;
        }

        public void setThumbMediaId(String thumbMediaId) {
            ThumbMediaId = thumbMediaId;
        }

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

        public String getMusicUrl() {
            return MusicUrl;
        }

        public void setMusicUrl(String musicUrl) {
            MusicUrl = musicUrl;
        }

        public String getHQMusicUrl() {
            return HQMusicUrl;
        }

        public void setHQMusicUrl(String musicUrl) {
            HQMusicUrl = musicUrl;
        }

    }
}
