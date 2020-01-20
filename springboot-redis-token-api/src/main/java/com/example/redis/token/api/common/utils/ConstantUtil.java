package com.example.redis.token.api.common.utils;

public class ConstantUtil {
    /**
     * 请求接口中header中的参数
     */
    public interface RequestHeader {
        String TOKEN = "token";
    }

    /**
     * redis自定义常量数据
     */
    public interface Redis {
        String TOKEN_PREFIX = "token:";
        Integer TOKEN_EXPIRE_TIME_SECOND = 60;// 过期时间, 60s, 一分钟
    }

    /**
     * 订单常量
     */
    public interface Order {
        String ORDER_ID_PREFIX = "orderId:";
        Integer ORDER_ID_PREFIX_TIME_SECOND = 60;// 过期时间, 60s, 一分钟

        //订单状态
        int PAYED = 0;
        int ORDER_CREATE = 1;
        int ORDER_OVER_TIME = 2;
        int ORDER_DELETE = 3;
    }
}
