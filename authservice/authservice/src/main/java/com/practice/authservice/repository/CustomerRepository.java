package com.practice.authservice.repository;

import com.practice.authservice.model.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
    Mono<Customer> findByEmail(String email);
}
