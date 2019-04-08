package com.rxiu.wechat.core.message.response;

/**
 * @author rxiu
 * @date 2018/07/19.
 **/
public class VoiceMessage {

    private Voice Voice;

    public Voice getVoice() {
        return Voice;
    }

    public void setVoice(Voice voice) {
        Voice = voice;
    }

    public static class Voice {

        private String MediaId;

        public String getMediaId() {
            return MediaId;
        }

        public void setMediaId(String mediaId) {
            MediaId = mediaId;
        }

    }

}
