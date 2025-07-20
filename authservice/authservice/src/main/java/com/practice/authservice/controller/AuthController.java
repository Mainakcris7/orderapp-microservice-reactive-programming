package com.practice.authservice.controller;

import com.practice.authservice.dto.CustomerLoginDto;
import com.practice.authservice.dto.CustomerRegisterDto;
import com.practice.authservice.dto.CustomerUpdateDto;
import com.practice.authservice.dto.JwtDto;
import com.practice.authservice.model.Customer;
import com.practice.authservice.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService service;

    // Register a new customer
    // POST http://localhost:7777/auth/register
    @PostMapping("/register")
    public Mono<ResponseEntity<Customer>> saveCustomer(@RequestBody CustomerRegisterDto customer){
        return service.saveCustomer(customer);
    }

    // Update an existing customer
    // PUT http://localhost:7777/auth/update
    @PutMapping("")
    public Mono<Customer> updateCustomer(@RequestBody CustomerUpdateDto customer){
        return service.updateCustomer(customer);
    }

    // Login a customer
    // POST http://localhost:7777/auth/login
    @PostMapping("/login")
    public Mono<JwtDto> loginCustomer(@RequestBody CustomerLoginDto dto){
        return service.loginCustomer(dto);
    }

    // Validate a JWT Token
    // POST http://localhost:7777/auth/validate
    @PostMapping("/validate")
    public Mono<Boolean> validateJwtToken(@RequestBody JwtDto jwtDto){
        return service.validateJwtToken(jwtDto);
    }
}
