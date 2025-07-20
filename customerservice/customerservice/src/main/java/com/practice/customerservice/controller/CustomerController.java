package com.practice.customerservice.controller;

import com.practice.customerservice.dto.AddCustomerOrderDto;
import com.practice.customerservice.model.Customer;
import com.practice.customerservice.model.CustomerOrder;
import com.practice.customerservice.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@AllArgsConstructor
public class CustomerController {
    private final CustomerService service;

    // Get all customers
    // GET http://localhost:7777/customers
    @GetMapping("")
    public Flux<Customer> getAllCustomers(){
        return service.getAllCustomers();
    }

    // Get customer by id
    // GET http://localhost:7777/customers/{id}
    @GetMapping("/{id}")
    public Mono<Customer> getCustomerById(@PathVariable Integer id){
        return service.getCustomerById(id);
    }

    // Add a new order to an existing customer
    // PUT http://localhost:7777/customers/add/order
    @PutMapping("/add/order")
    public Mono<CustomerOrder> addOrderToCustomer(@RequestBody AddCustomerOrderDto dto){
        return service.addOrderToCustomer(dto);
    }

    // Remove an order from an existing customer
    // PUT http://localhost:7777/customers/remove/order
    @PutMapping("/remove/order/{orderId}")
    public Mono<Void> removeOrderFromCustomer(@PathVariable Integer orderId){
        return service.removeOrderFromCustomer(orderId);
    }

    // Delete a customer by id
    // DELETE http://localhost:7777/customers/{id}
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomerById(@PathVariable Integer id){
        return service.deleteCustomerById(id);
    }

}
