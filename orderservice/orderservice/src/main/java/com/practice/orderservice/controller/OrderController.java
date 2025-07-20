package com.practice.orderservice.controller;

import com.practice.orderservice.dto.OrderSaveDto;
import com.practice.orderservice.dto.OrderUpdateDto;
import com.practice.orderservice.model.Order;
import com.practice.orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService service;

    // GET http://localhost:7777/customers
    // Get all orders
    @GetMapping("")
    public Flux<Order> getAllOrders(){
        return service.getAllOrders();
    }

    // GET http://localhost:7777/customers/{id}
    // Get order by id
    @GetMapping("/{id}")
    public Mono<Order> getOrderById(@PathVariable int id){
        return service.getOrderById(id);
    }

    // POST http://localhost:7777/customers
    // Create a new order
    @PostMapping("")
    public Mono<ResponseEntity<Order>> saveOrder(@RequestBody OrderSaveDto dto){
        return service.saveOrder(dto);
    }

    // PUT http://localhost:7777/customers
    // Update an existing order
    @PutMapping("")
    public Mono<Order> updateOrder(@RequestBody OrderUpdateDto dto){
        return service.updateOrder(dto);
    }

    // DELETE http://localhost:7777/customers/{id}
    // Delete an order by id
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteOrderById(@PathVariable int id){
        return service.deleteOrderById(id);
    }

}
