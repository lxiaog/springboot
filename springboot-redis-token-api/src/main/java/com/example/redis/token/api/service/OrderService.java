package com.example.redis.token.api.service;

import com.example.redis.token.api.common.rest.RestResult;
import com.example.redis.token.api.jpa.entity.OrderEntity;

public interface OrderService {

    /**
     * 创建订单
     * @param orderId
     * @return
     */
    RestResult createOrder(String orderId);

    /**
     * 查询订单
     * @param orderId
     * @return
     */
    OrderEntity getByOrderId(String orderId);


}
