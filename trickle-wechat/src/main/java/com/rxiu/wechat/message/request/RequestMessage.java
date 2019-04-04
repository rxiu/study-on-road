package com.rxiu.wechat.message.request;


import com.rxiu.wechat.message.Message;

/**
 * @author rxiu
 * @date 2018/07/19.
 **/
public class RequestMessage extends Message {
    private long MsgId;

    public long getMsgId() {
        return MsgId;
    }

    public void setMsgId(long msgId) {
        MsgId = msgId;
    }
}
