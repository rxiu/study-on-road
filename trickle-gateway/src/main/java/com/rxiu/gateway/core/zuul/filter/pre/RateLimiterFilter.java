package com.rxiu.gateway.core.zuul.filter.pre;

import com.google.common.util.concurrent.RateLimiter;
import com.rxiu.gateway.core.zuul.FilterOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public class RateLimiterFilter extends AbstractPreZuulFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimiterFilter.class);

    /**
     * 每秒允许处理的量是5
     */
    RateLimiter rateLimiter = RateLimiter.create(5);

    @Override
    public int filterOrder() {
        return FilterOrder.RATE_LIMITER_ORDER;
    }

    @Override
    public Object doRun() {
        HttpServletRequest request = context.getRequest();
        String url = request.getRequestURI();
        if (rateLimiter.tryAcquire()) {
            return success();
        } else {
            LOGGER.info("rate limit:{}", url);
            return fail(401, String.format("rate limit:{}", url));
        }
    }
}
