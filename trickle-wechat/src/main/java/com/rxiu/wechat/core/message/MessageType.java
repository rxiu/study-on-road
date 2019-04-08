package com.rxiu.wechat.core.message;

/**
 * @author rxiu
 * @date 2018/07/19.
 **/
public class MessageType {

    public enum Request {
        text, image, link, location, voice, vedio, shortvedio, event;
    }

    public enum Response {
        text, music, news, image, voice, vedio;
    }

    public enum Event {
        subscribe, unsubscribe, SCAN, LOCATION, CLICK, VIEW;
    }
}
