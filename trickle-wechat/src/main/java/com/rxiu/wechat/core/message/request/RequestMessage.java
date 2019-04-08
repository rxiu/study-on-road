package com.rxiu.wechat.core.message.request;


import com.rxiu.wechat.core.message.Message;

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
