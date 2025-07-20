package com.practice.orderservice.service;

import com.practice.orderservice.dto.ProductPriceDto;
import com.practice.orderservice.dto.ProductSaveDto;
import com.practice.orderservice.dto.ProductUpdateDto;
import com.practice.orderservice.model.Product;
import com.practice.orderservice.repository.ProductRepository;
import com.practice.orderservice.util.ProductMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository repo;

    // Get all products
    public Flux<Product> getAllProducts(){
        return repo.findAll()
                .switchIfEmpty(Mono.error(new RuntimeException("No products found!")));
    }

    // Get product by id
    public Mono<Product> getProductById(Integer id){
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found with id: " + id)));
    }

    // Save a new product
    public Mono<ResponseEntity<Product>> saveProduct(ProductSaveDto dto){
        Product product = ProductMapperUtil.fromSaveDto(dto); // Map DTO to Product class
        return repo.save(product)
                .map(p -> ResponseEntity.status(HttpStatus.CREATED).body(p))
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to save the product!")));
    }

    // Update an existing product
    public Mono<Product> updateProduct(ProductUpdateDto dto){
        Product product = ProductMapperUtil.fromUpdateDto(dto);  // Map DTO to Product class
        return repo.save(product)
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to update the product!")));
    }

    // Delete a product by id
    public Mono<ResponseEntity<Void>> deleteProductById(Integer id){
        return repo.existsById(id)
                .flatMap(exists -> {
                    if(exists){
                        return repo.deleteById(id)
                                .then(Mono.just(ResponseEntity.noContent().build()));
                    }else{
                        return Mono.error(new RuntimeException("Product not found with id: " + id));
                    }
                });

    }

    // Add stock/quantity for a product
    Mono<Product> addStock(Integer id, int byQuantity){
        return repo.findById(id)
                .doOnNext(product -> {
                    product.setStockQuantity(product.getStockQuantity() + byQuantity);
                })
                .flatMap(repo::save);
    }

    // Reduce stock/quantity for a product
    Mono<Product> removeStock(Integer id, int byQuantity){
        return repo.findById(id)
                .doOnNext(product -> {
                    product.setStockQuantity(product.getStockQuantity() - byQuantity);
                })
                .flatMap(repo::save);
    }

    // Get total price of a product based on quantity
    // Steps -
    // 1. Fetch the product by id
    // 2. Check if the quantity is invalid (more than the available quantity?)
    // 3. Return totalPrice with productName (if valid)
    // 4. Return a failing Mono (if invalid)
    Mono<ProductPriceDto> getProductTotalPriceById(Integer id, int quantity){
        return repo.findById(id)
                .flatMap(product -> {
                    if(product.getStockQuantity() >= quantity){
                        product.setStockQuantity(product.getStockQuantity() - quantity);
                        return repo.save(product)
                                .then(
                                        Mono.just(ProductPriceDto.builder().totalPrice(product.getPrice() * quantity).productName(product.getProductName()).build())
                                );
                    }else{
                        return Mono.error(new RuntimeException("Invalid quantity"));
                    }
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Product not found with id: " + id)));
    }
}
