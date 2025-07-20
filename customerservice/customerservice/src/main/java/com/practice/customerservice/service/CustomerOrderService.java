package com.practice.customerservice.service;

import com.practice.customerservice.model.CustomerOrder;
import com.practice.customerservice.repository.CustomerOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CustomerOrderService {
    private final CustomerOrderRepository repo;

    // To add a new entry in "customer_orders" table
    // To represent the order(s) of a specific customer
    Mono<CustomerOrder> addCustomerOrder(Integer customerId, Integer orderId){
        CustomerOrder customerOrder = new CustomerOrder(null, orderId, customerId);
        return repo.save(customerOrder)
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to save the new order for the customer with id: " + customerId)));
    }

    // To remove an order from a customer
    Mono<Void> removeCustomerOrder(Integer orderId) {
        return repo.existsByOrderId(orderId)
                .flatMap(exists -> {
                    if(exists)
                        return repo.deleteByOrderId(orderId)
                                .onErrorResume(ex -> Mono.error(new RuntimeException("Failed to delete the order with id: " + orderId)));
                    else
                        return Mono.error(new RuntimeException("No order found with id: " + orderId));
                });
    }

    // Find the orders of a customer
    Flux<CustomerOrder> getOrdersByCustomerId(Integer id){
        return repo.findByCustomerId(id);
    }
}
