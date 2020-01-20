package com.example.redis.token.api.controller;

import com.example.redis.token.api.anno.ApiIdempotent;
import com.example.redis.token.api.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Work
 */
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiIdempotent
    @PostMapping(value = "/create", produces = "application/json;charset=UTF-8")
    public Object getToken() {
        String orderId = UUID.randomUUID().toString().trim().replaceAll("-", "");
        return orderService.createOrder(orderId);
    }
}
