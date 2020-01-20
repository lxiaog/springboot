package com.example.redis.token.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.redis.token.api.common.rest.ResponseCode;
import com.example.redis.token.api.common.rest.RestResult;
import com.example.redis.token.api.common.utils.ConstantUtil;
import com.example.redis.token.api.config.redis.RedisCache;
import com.example.redis.token.api.exception.ParamException;
import com.example.redis.token.api.jpa.entity.OrderEntity;
import com.example.redis.token.api.jpa.repository.OrderRepository;
import com.example.redis.token.api.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RedisCache redisCache;

    @Override
    public RestResult createOrder(String orderId) {
        try {
            log.info("[order_id:" + orderId + "]创建订单");
            //订单查询 先缓存 再数据库
            OrderEntity order = getByOrderId(orderId);
            if (Objects.nonNull(order)) {
                log.info("[order_id:" + orderId + "]该订单已经存在，不能重复创建");
                return RestResult.fail().setCode(ResponseCode.ORDER_IS_EXIST.getCode()).setMsg(ResponseCode.ORDER_IS_EXIST.getMsg());
            }
            //如果为空初始化订单
            order = new OrderEntity();
            order.setOrderId(orderId)
                    .setOrderName("2019款，小米电脑13.3")
                    .setOrderDesc("2019款 小米电脑13.3 内存8G 固态硬盘256G")
                    .setCreateTime(new Date())
                    .setUpdateTime(new Date())
                    .setOrderStatus(ConstantUtil.Order.ORDER_CREATE);
            OrderEntity newOrder = orderRepository.save(order);
            if (newOrder == null || StringUtils.isEmpty(newOrder.getOrderId())) {
                log.warn("[order_id:" + orderId + "]创建订单失败");
                return RestResult.fail().setMsg("创建订单失败");
            }
            //放入缓存
            redisCache.setMap(ConstantUtil.Order.ORDER_ID_PREFIX.concat(orderId), newOrder, ConstantUtil.Order.ORDER_ID_PREFIX_TIME_SECOND);
            log.info("[order_id:" + orderId + "]订单创建成功");
            return RestResult.ok(newOrder).setMsg("订单创建成功");
        } catch (ParamException e) {
            log.error("[order_id:" + orderId + "]创建订单异常：" + e.getMessage());
            e.printStackTrace();
            return RestResult.error().setMsg(e.getMessage());
        } catch (Exception e) {
            log.error("[order_id:" + orderId + "]创建订单异常：" + e.getMessage());
            e.printStackTrace();
            return RestResult.error().setMsg("创建订单异常");
        }

    }

    @Override
    public OrderEntity getByOrderId(String orderId) {
        OrderEntity orderEntity = null;
        String orderKey = ConstantUtil.Order.ORDER_ID_PREFIX.concat(orderId);
        boolean isOrderId = redisCache.hasKey(orderKey);
        if (isOrderId) {
            orderEntity = redisCache.getMap(orderKey, OrderEntity.class);
            if (orderEntity != null && !StringUtils.isEmpty(orderEntity.getOrderId())) {
                log.info("缓存中存在该订单[order_id:" + orderId + "]:entity:" + JSON.toJSONString(orderEntity));
                return orderEntity;
            }
        }
        log.info("[order_id:" + orderId + "]该订单不再redis中，从数据库中读取");
        //如果缓存中没有  数据库中有 要么是支付完成的订单 要么是已经超时的订单，不再放入缓存中
        Optional<OrderEntity> optional = orderRepository.queryByOrderId(orderId);
        return optional.orElse(null);
    }
}
