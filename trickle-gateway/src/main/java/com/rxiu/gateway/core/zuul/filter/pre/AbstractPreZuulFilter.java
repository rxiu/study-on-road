package com.rxiu.gateway.core.zuul.filter.pre;

import com.rxiu.gateway.core.zuul.FilterType;
import com.rxiu.gateway.core.zuul.filter.AbstractZuulFilter;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public abstract class AbstractPreZuulFilter extends AbstractZuulFilter {
    @Override
    public String filterType() {
        return FilterType.pre.name();
    }
}
