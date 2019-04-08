package com.rxiu.wechat.common.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rxiu
 * @date 2019/4/8
 */
public class XmlUtil {

    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        Map<String, String> map = new HashMap<>(8);

        InputStream inputStream = request.getInputStream();
        Document document = new SAXReader().read(inputStream);
        List<Element> elementList = document.getRootElement().elements();

        for (Element e : elementList) {
            map.put(e.getName(), e.getText());
        }

        inputStream.close();
        inputStream = null;

        return map;
    }

    public static String messageToXml(Object message) {
        return messageToXml(message, null);
    }

    public static String messageToXml(Object message, Node node) {
        xstream.alias("xml", message.getClass());
        if (node != null && !node.isEmpty()) {
            node.entrySet().forEach(i -> {
                xstream.alias(i.getKey(), i.getValue());
            });
        }
        return xstream.toXML(message);
    }

    public static class Node extends HashMap<String, Class> {
        public Node push(String key, Class clazz) {
            this.put(key, clazz);
            return this;
        }
    }

    private static XStream xstream = new XStream(new XppDriver() {
        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                boolean cdata = true;

                @Override
                public void startNode(String name, Class clazz) {
                    cdata = !"CreateTime".equals(name);
                    super.startNode(name, clazz);
                }

                @Override
                protected void writeText(QuickWriter writer, String text) {
                    writer.write(cdata ? String.format("<![CDATA[%s]]>", text) : text);
                }
            };
        }
    });
}