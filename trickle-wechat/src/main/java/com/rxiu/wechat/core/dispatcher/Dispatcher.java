package com.rxiu.wechat.core.dispatcher;

import java.util.Map;

/**
 * @author rxiu
 * @date 2019/4/4
 */
public interface Dispatcher {

    String process(Map<String, String> map);
}
