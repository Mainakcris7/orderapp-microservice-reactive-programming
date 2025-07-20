package com.practice.orderservice.controller;

import com.practice.orderservice.dto.ProductSaveDto;
import com.practice.orderservice.dto.ProductUpdateDto;
import com.practice.orderservice.model.Product;
import com.practice.orderservice.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService service;

    // GET http://localhost:7777/products
    // Get all products
    @GetMapping("")
    public Flux<Product> getAllProducts(){
        return service.getAllProducts();
    }

    // GET http://localhost:7777/products/{id}
    // Get product by id
    @GetMapping("/{id}")
    public Mono<Product> getProductById(@PathVariable Integer id){
        return service.getProductById(id);
    }

    // POST http://localhost:7777/products
    // Create a new product
    @PostMapping("")
    public Mono<ResponseEntity<Product>> saveProduct(@RequestBody ProductSaveDto dto){
        return service.saveProduct(dto);
    }

    // PUT http://localhost:7777/products
    // Update an existing product
    @PutMapping("")
    public Mono<Product> updateProduct(@RequestBody ProductUpdateDto dto){
        return service.updateProduct(dto);
    }

    // DELETE http://localhost:7777/products/{id}
    // Delete a product by id
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteProductById(@PathVariable Integer id){
        return service.deleteProductById(id);
    }
}
