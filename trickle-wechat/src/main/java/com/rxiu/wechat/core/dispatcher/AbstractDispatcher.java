package com.rxiu.wechat.core.dispatcher;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.rxiu.wechat.common.Constant;

import java.util.Map;

/**
 * @author rxiu
 * @date 2019/4/4
 */
public abstract class AbstractDispatcher implements Dispatcher {

    @Override
    public String process(Map<String, String> map) {
        String from = Preconditions.checkNotNull(map.get(Constant.WECHAT_FROM_USER_NAME));
        String to = Preconditions.checkNotNull(map.get(Constant.WECHAT_TO_USER_NAME));
        Preconditions.checkArgument(map.containsKey(Constant.WECHAT_MSG_TYPE) && !Strings.isNullOrEmpty(map.get(Constant.WECHAT_MSG_TYPE)));

        return doProcess(map, from, to);
    }

    protected abstract String doProcess(Map<String, String> map, String from, String to);
}
