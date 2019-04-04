package com.rxiu.wechat.common.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.rxiu.wechat.compent.Menu;
import com.rxiu.wechat.core.PropertyPlaceHolder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author rxiu
 * @date 2018/07/23.
 **/
public class WeChatUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeChatUtil.class);

    /**
     * token工具类
     */
    public static class TokenTool {
        private static final String ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY";
        private static final long ACCESS_TOKEN_EXPIRE = 2 * 60 * 1000;

        private static String accessTokenUrl = PropertyPlaceHolder.getString("wechat.access-token.url");

        private static String getAccessTokenUrl() {
            accessTokenUrl = accessTokenUrl
                    .replace("${wechat.app-id}", PropertyPlaceHolder.getString("wechat.app-id"))
                    .replace("${wechat.app-secret}", PropertyPlaceHolder.getString("wechat.app-secret"));
            return accessTokenUrl;
        }

        public static String getAccessToken() {
            String token = HttpUtil.sendGet(getAccessTokenUrl(), null);
            LOGGER.info("获取access token: {}", token);
            if (!Strings.isNullOrEmpty(token)) {
                String accessToken = JSONObject.parseObject(token).getString("access_token");
                if (!Strings.isNullOrEmpty(accessToken)) {
                    RedisUtil.builder().set(ACCESS_TOKEN_KEY, accessToken, ACCESS_TOKEN_EXPIRE);
                    return accessToken;
                }
            }
            return null;
        }

        public static String getAccessTokenFromRedis() {
            String accessToken = RedisUtil.builder().get(ACCESS_TOKEN_KEY);
            if (Strings.isNullOrEmpty(accessToken)) {
                accessToken = getAccessToken();
                if (Strings.isNullOrEmpty(accessToken)) return null;
                RedisUtil.builder().set(ACCESS_TOKEN_KEY, accessToken, ACCESS_TOKEN_EXPIRE);
            }
            return accessToken;
        }
    }

    /**
     * 签名工具类
     */
    public static class SignatureTool {
        private static final Logger LOGGER = LoggerFactory.getLogger(SignatureTool.class);
        private static final String token = PropertyPlaceHolder.getString("wechat.token");

        /**
         * 验证签名
         *
         * @param signature
         * @param timestamp
         * @param nonce
         * @return
         */
        public static boolean checkSignature(String signature, String timestamp, String nonce) {
            String[] arr = new String[]{token, timestamp, nonce};
            Arrays.sort(arr);
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < arr.length; i++) {
                buffer.append(arr[i]);
            }

            String tmpStr = null;
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
                byte[] digest = messageDigest.digest(buffer.toString().getBytes());
                tmpStr = byteToStr(digest);
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error("check singnature error.{}", e);
            }
            return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
        }

        /**
         * 将字节数组转换为十六进制字符串
         *
         * @param byteArray
         * @return
         */
        private static String byteToStr(byte[] byteArray) {
            String strDigest = "";
            for (int i = 0; i < byteArray.length; i++) {
                strDigest += byteToHexStr(byteArray[i]);
            }
            return strDigest;
        }

        /**
         * 将字节转换为十六进制字符串
         *
         * @param mByte
         * @return
         */
        private static String byteToHexStr(byte mByte) {
            char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            char[] tempArr = new char[2];
            tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
            tempArr[1] = Digit[mByte & 0X0F];
            String s = new String(tempArr);
            return s;
        }
    }


    public static class MessageTool {

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
            public Node push (String key, Class clazz) {
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

    /**
     * 菜单工具类
     */
    public static class MenuTool {
        private static String menuCreateUrl = PropertyPlaceHolder.getString("wechat.menu.create.url");

        public static int createMenu(Object menu) {
            String json = "";
            if (menu instanceof Menu) {
                json = JSONObject.toJSONString(menu);
            }
            if (menu instanceof String) {
                json = menu.toString();
            }
            String accessToken = WeChatUtil.TokenTool.getAccessTokenFromRedis();

            String url = HttpUtil.buildUrl(menuCreateUrl, Collections.singletonMap("access_token", accessToken));
            String result = HttpUtil.sendPostBuffer(url, json);
            LOGGER.info("创建微信菜单: {}", result);
            return Strings.isNullOrEmpty(result) ? Integer.MIN_VALUE : JSONObject.parseObject(result).getInteger("errcode");
        }
    }
}
