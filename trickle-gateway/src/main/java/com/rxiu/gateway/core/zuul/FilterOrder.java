package com.rxiu.gateway.core.zuul;

/**
 * @author rxiu
 * @date 2019/4/3
 */
public class FilterOrder {
    public static final Integer TOKEN_ACCESS_ORDER = 0;
    public static final Integer USER_RIGHT_ORDER = 1;
    public static final Integer RATE_LIMITER_ORDER = 2;
}
