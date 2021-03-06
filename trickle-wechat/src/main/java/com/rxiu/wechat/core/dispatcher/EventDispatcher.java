package com.rxiu.wechat.core.dispatcher;

import com.rxiu.wechat.common.Constant;
import com.rxiu.wechat.common.util.XmlUtil;
import com.rxiu.wechat.core.message.MessageType;
import com.rxiu.wechat.core.message.response.NewsMessage;
import com.rxiu.wechat.core.message.response.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author rxiu
 * @date 2018/07/19.
 **/
public class EventDispatcher extends AbstractDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventDispatcher.class);

    public String doProcess(Map<String, String> map, String from, String to) {
        String event = map.get(Constant.WECHAT_EVENT);
        LOGGER.info("事件类型：{}", event);

        if (MessageType.Event.subscribe.name().equals(event)) {
            return XmlUtil.messageToXml(new TextMessage(to, from, "感谢关注"));
        }
        if (MessageType.Event.unsubscribe.name().equals(event)) {
            return XmlUtil.messageToXml(new TextMessage(to, from, "取消关注"));
        }
        if (MessageType.Event.SCAN.name().equals(event)) {
            return XmlUtil.messageToXml(new TextMessage(to, from, "触发扫描"));
        }
        if (MessageType.Event.LOCATION.name().equals(event)) {
            return XmlUtil.messageToXml(new TextMessage(to, from, "定位"));
        }
        if (MessageType.Event.CLICK.name().equals(event)) {
            NewsMessage message = new NewsMessage();
            message.setToUserName(from);
            message.setFromUserName(to);
            message.setCreateTime(System.currentTimeMillis());
            message.setMsgType(MessageType.Response.news.name());

            NewsMessage.Article article = new NewsMessage.Article();
            article.setDescription("welcome to my github.");
            article.setPicUrl("https://avatar.csdn.net/1/6/B/1_ragin.jpg?1531992375");
            article.setTitle("Rxiu Github");
            article.setUrl("https://github.com/rxiu");
            List<NewsMessage.Article> list = new ArrayList<>();
            list.add(article);

            article = new NewsMessage.Article();
            article.setDescription("welcome to my gitee.");
            article.setPicUrl("https://gitee.com/logo-black.svg?20171024");
            article.setTitle("Braska gitee");
            article.setUrl("https://gitee.com/syher");
            list.add(article);

            article = new NewsMessage.Article();
            article.setDescription("welcome to my cnblogs.");
            article.setPicUrl("https://pic.cnblogs.com/avatar/736377/20190118165442.png");
            article.setTitle("Aruze cnblogs");
            article.setUrl("https://www.cnblogs.com/braska/");
            list.add(article);

            message.setArticleCount(list.size());
            message.setArticles(list);
            return XmlUtil.messageToXml(message,
                    new XmlUtil.Node().push("item", NewsMessage.Article.class)
            );
        }
        if (MessageType.Event.VIEW.name().equals(event)) {
            return XmlUtil.messageToXml(new TextMessage(to, from, "视图"));
        }
        return null;
    }
}
