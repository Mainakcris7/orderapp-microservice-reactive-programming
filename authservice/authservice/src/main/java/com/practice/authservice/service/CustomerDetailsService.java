package com.practice.authservice.service;

import com.practice.authservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerDetailsService implements ReactiveUserDetailsService {

    @Autowired
    private CustomerRepository repo;

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return repo.findByEmail(email)
                .map(customer -> (UserDetails) new CustomerDetails(customer))
                .switchIfEmpty(Mono.error(new RuntimeException("No customer found with email: " + email)));
    }
}
