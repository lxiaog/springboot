package com.example.redis.token.api.jpa.repository;

import com.example.redis.token.api.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Integer>, JpaSpecificationExecutor<OrderEntity> {

    Optional<OrderEntity> queryByOrderId(String orderId);
}
