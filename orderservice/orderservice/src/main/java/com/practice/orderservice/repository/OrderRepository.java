package com.practice.orderservice.repository;

import com.practice.orderservice.model.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrderRepository extends ReactiveCrudRepository<Order, Integer> {
}
