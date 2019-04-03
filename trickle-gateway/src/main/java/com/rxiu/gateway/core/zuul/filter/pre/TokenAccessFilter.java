package com.rxiu.gateway.core.zuul.filter.pre;

import com.google.common.base.Strings;
import com.rxiu.gateway.core.zuul.FilterOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public class TokenAccessFilter extends AbstractPreZuulFilter {

    @Value("${token.verify:false}")
    private boolean verify;

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenAccessFilter.class);

    @Override
    public int filterOrder() {
        return FilterOrder.TOKEN_ACCESS_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return verify;
    }

    @Override
    public Object doRun() {
        HttpServletRequest request = context.getRequest();

        if ("PUT".equalsIgnoreCase(request.getMethod())) {
            InputStream is;
            BufferedReader in;
            String param = "";
            try {
                is = request.getInputStream();
                if (is != null) {
                    in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String strRead;
                    StringBuilder resultSb = new StringBuilder("");
                    while ((strRead = in.readLine()) != null) {
                        resultSb.append(strRead);
                    }
                    param = resultSb.toString();
                }

                String[] params = param.split("&");
                if (params.length > 0) {
                    String keyAndValue = Arrays.stream(params).filter(kv -> kv.contains("token")).findAny().orElse("");
                    if (Strings.isNullOrEmpty(keyAndValue)) {
                        LOGGER.info("token为空!");
                        return fail(401, "token must be specify.");
                    } else {
                        String[] tokenKeyAndValue = keyAndValue.split("=");
                        if (tokenKeyAndValue.length < 2 || !"token".equals(tokenKeyAndValue[0])) {
                            LOGGER.info("token为空!");
                            return fail(401, "token must be specify.");
                        } else {
                            return success();
                        }
                    }
                } else {
                    LOGGER.info("token为空!");
                    return fail(401, "token must be specify.");
                }
            } catch (IOException e) {
                LOGGER.info("获取请求参数时异常:{}", e);
                return fail(401, "exception for get parameter.");
            }
        } else {

            Map<String, String[]> paramArr = request.getParameterMap();
            if (!paramArr.containsKey("token")) {
                LOGGER.info("token为空!");
                return fail(401, "token must be specify.");
            }
        }

        return success();
    }
}
