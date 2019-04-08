package com.rxiu.wechat.common.util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

/**
 * @author rxiu
 * @date 2018/07/20.
 **/
public class HttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    public static String sendGet(String reqUrl, Map<String, String> params) {
        LOGGER.info("请求地址：{}", reqUrl);
        LOGGER.info("请求参数：{}", params);
        InputStream inputStream = null;
        HttpGet request = new HttpGet();
        try {
            String url = buildUrl(reqUrl, params);
            HttpClient client = new DefaultHttpClient();

            request.setHeader("Accept-Encoding", "gzip");
            request.setURI(new URI(url));

            HttpResponse response = client.execute(request);

            inputStream = response.getEntity().getContent();
            String result = getJsonStringFromGZIP(inputStream);
            LOGGER.info("返回结果-》｛｝", result);
            return result;
        } catch (Exception e) {
            LOGGER.info("error send get request.{}", e);
            return null;
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // ignore
                }
            }
            request.releaseConnection();
        }

    }

    public static String sendPost(String reqUrl, Map<String, String> params) {
        LOGGER.info("请求地址：{}", reqUrl);
        LOGGER.info("请求参数：{}", params);
        try {
            Set<String> set = params.keySet();
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for (String key : set) {
                list.add(new BasicNameValuePair(key, params.get(key)));
            }
            if (list.size() > 0) {
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost request = new HttpPost(reqUrl);

                    request.setHeader("Accept-Encoding", "gzip");
                    request.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));

                    HttpResponse response = client.execute(request);

                    InputStream inputStream = response.getEntity().getContent();
                    try {
                        String result = getJsonStringFromGZIP(inputStream);
                        LOGGER.info("返回结果-》｛｝", result);
                        return result;
                    } finally {
                        inputStream.close();
                    }
                } catch (Exception ex) {
                    LOGGER.info("connect refused.{}", ex);
                    return null;
                }
            } else {
                throw new Exception("参数不全，请稍后重试");
            }
        } catch (Exception ex) {
            LOGGER.info("error send post request.{}", ex);
            return null;
        }
    }

    public static String sendPostBuffer(String urls, String params){
        LOGGER.info("请求地址：{}", urls);
        LOGGER.info("请求参数：{}", params);
        String retSrc = null;
        HttpPost request = new HttpPost(urls);
        try {
            StringEntity se = new StringEntity(params, HTTP.UTF_8);
            request.setEntity(se);
            HttpResponse httpResponse = new DefaultHttpClient().execute(request);
            retSrc = EntityUtils.toString(httpResponse.getEntity());
            LOGGER.info("返回结果-》｛｝", retSrc);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            request.releaseConnection();
        }
        return retSrc;

    }

    public static String sendXmlPost(String urlStr, String xmlInfo) {
        try {
            URL url = new URL(urlStr);
            URLConnection con = url.openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("Pragma:", "no-cache");
            con.setRequestProperty("Cache-Control", "no-cache");
            con.setRequestProperty("Content-Type", "text/xml");
            OutputStreamWriter out = new OutputStreamWriter(
                    con.getOutputStream());
            out.write(new String(xmlInfo.getBytes("utf-8")));
            out.flush();
            out.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String lines = "";
            for (String line = br.readLine(); line != null; line = br
                    .readLine()) {
                lines = lines + line;
            }
            return lines;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "fail";
    }

    private static String getJsonStringFromGZIP(InputStream is) {
        String jsonString = null;
        try {
            BufferedInputStream bis = new BufferedInputStream(is);
            bis.mark(2);
            byte[] header = new byte[2];
            int result = bis.read(header);
            bis.reset();
            int headerData = getShort(header);
            if (result != -1 && headerData == 0x1f8b) {
                is = new GZIPInputStream(bis);
            } else {
                is = bis;
            }
            InputStreamReader reader = new InputStreamReader(is, "utf-8");
            char[] data = new char[100];
            int readSize;
            StringBuffer sb = new StringBuffer();
            while ((readSize = reader.read(data)) > 0) {
                sb.append(data, 0, readSize);
            }
            jsonString = sb.toString();
            bis.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonString;
    }

    private static int getShort(byte[] data) {
        return (data[0] << 8) | data[1] & 0xFF;
    }

    public static String buildUrl(String reqUrl, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return reqUrl;
        }
        StringBuilder query = new StringBuilder();
        Set<String> set = params.keySet();
        for (String key : set) {
            query.append(String.format("%s=%s&", key, params.get(key)));
        }
        return reqUrl + "?" + query.toString();
    }
}
