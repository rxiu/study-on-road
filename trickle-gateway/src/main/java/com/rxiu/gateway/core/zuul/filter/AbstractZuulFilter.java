package com.rxiu.gateway.core.zuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.rxiu.gateway.core.zuul.ContantValue;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public abstract class AbstractZuulFilter extends ZuulFilter {

    protected RequestContext context;

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return (boolean) (ctx.getOrDefault(ContantValue.NEXT_FILTER, true));
    }

    @Override
    public Object run() {
        context = RequestContext.getCurrentContext();
        return doRun();
    }

    public abstract Object doRun();

    public Object fail(Integer code, String message) {
        context.set(ContantValue.NEXT_FILTER, false);
        context.setSendZuulResponse(false);
        context.getResponse().setContentType("text/html;charset=UTF-8");
        context.setResponseStatusCode(code);
        context.setResponseBody(String.format("{\"result\":\"%s!\"}", message));
        return null;
    }

    public Object success() {
        context.set(ContantValue.NEXT_FILTER, true);
        return null;
    }
}
