package com.rxiu.wechat.core.dispatcher;

import com.rxiu.wechat.common.Constant;
import com.rxiu.wechat.core.message.MessageType;

import java.util.Map;

/**
 * @author rxiu
 * @date 2019/4/4
 */
public class DispatcherBuilder {

    private Map<String, String> map;
    private Dispatcher dispatcher;

    private static class SINGLETON {
        private static final DispatcherBuilder BUILDER = new DispatcherBuilder();
    }

    private static DispatcherBuilder getBuilder() {
        return SINGLETON.BUILDER;
    }

    public static DispatcherBuilder configure(Map<String, String> map) {
        getBuilder().map = map;
        return getBuilder();
    }

    public Dispatcher build() {
        String msgType = map.get(Constant.WECHAT_MSG_TYPE);
        if (MessageType.Request.event.name().equals(msgType)) {
            dispatcher = new EventDispatcher();
        } else {
            dispatcher = new MessageDispatcher();
        }
        return dispatcher;
    }
}
