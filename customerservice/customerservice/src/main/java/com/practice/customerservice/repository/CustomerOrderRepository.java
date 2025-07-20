package com.practice.customerservice.repository;

import com.practice.customerservice.model.CustomerOrder;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerOrderRepository extends ReactiveCrudRepository<CustomerOrder, Integer> {
    Flux<CustomerOrder> findByCustomerId(Integer customerId);
    Mono<Void> deleteByOrderId(Integer orderId);
    Mono<Boolean> existsByOrderId(Integer orderId);
}
