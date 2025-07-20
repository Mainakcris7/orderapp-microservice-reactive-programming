package com.practice.orderservice.service;

import com.practice.orderservice.dto.*;
import com.practice.orderservice.model.Order;
import com.practice.orderservice.repository.OrderRepository;
import com.practice.orderservice.util.OrderMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository repo;
    private final ProductService productService;
    private final WebClient.Builder webClient;

    // Get all orders
    public Flux<Order> getAllOrders(){
        return repo.findAll()
                .switchIfEmpty(Mono.error(new RuntimeException("No orders found!")));
    }

    // Get order by id
    public Mono<Order> getOrderById(Integer id){
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found with id: " + id)));
    }

    // Save a new order
    /* Steps -
    * 1. Create an empty Order object
    * 2. Get the total price for the order based on product id and quantity
    * 3. Set the total price & order time
    * 4. Try to save the order
    * 4.1. If save fails, add the stock back to the product and raise exception
    * 4.2. If save succeeds -
    * 4.2.1. Send request to Customer Service to add order for a customer
    * 4.2.2. If the request fails, add back the stock and raise exception (Rollback)
    * 4.2.3. Else, send success message
    * */
    public Mono<ResponseEntity<Order>> saveOrder(OrderSaveDto dto){
        Order order = OrderMapperUtil.fromSaveDto(dto);  // Map DTO class to Order class

        Mono<ProductPriceDto> productTotalPrice = productService.getProductTotalPriceById(dto.getProductId(), dto.getQuantity());
        return productTotalPrice.flatMap(
                priceDto -> {
                    order.setTotalPrice(priceDto.getTotalPrice());
                    order.setOrderTime(LocalDateTime.now());
                    return repo.save(order)
                            .onErrorResume(ex -> {
                                return productService.addStock(dto.getProductId(), dto.getQuantity())
                                        .then(Mono.error(ex));
                            })
                            .flatMap(o -> webClient.build()
                                    .put()
                                    .uri("http://customerservice/customers/add/order")
                                    .bodyValue(new AddCustomerOrderDto(order.getCustomerId(), order.getOrderId(), priceDto.getProductName(), order.getQuantity(), priceDto.getTotalPrice()))
                                    .retrieve()
                                    .onStatus(HttpStatusCode::isError, response ->
                                            Mono.error(new RuntimeException("Failed to add order to customer"))
                                    )
                                    .bodyToMono(CustomerOrderDto.class)
                                    .onErrorResume(ex ->
                                            repo.deleteById(o.getOrderId())
                                            .then(productService.addStock(o.getProductId(), o.getQuantity()))
                                            .then(Mono.error(ex)))
                                    .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(o))
                            )
                            .switchIfEmpty(Mono.error(new RuntimeException("Failed to save the order!")));
                }
        );
    }

    // Update an existing order
    /* Steps -
    * 1. Fetch the order by id from DB
    * 2. Get the oldQuantity and newQuantity
    * 3. If oldQuantity >= newQuantity then,
    * 3.1. Get price per quantity (unit price)
    * 3.2. Based on the newQuantity and unit price, calculate the total price
    * 3.3. Add back the remaining quantity to the product
    * 3*. If oldQuantity < newQuantity
    * 3.4. Calculate the extra price for the extra quantity
    * 4. Set the price to the order & save
    * */
    public Mono<Order> updateOrder(OrderUpdateDto dto){
        return repo.findById(dto.getOrderId())
                .flatMap(order -> {
                    int oldQty = order.getQuantity();
                    int newQty = dto.getQuantity();

                    if(oldQty >= newQty){
                        int unitPrice = order.getTotalPrice() / oldQty;

                        order.setTotalPrice(unitPrice * dto.getQuantity());
                        order.setQuantity(dto.getQuantity());

                        return productService.addStock(
                                order.getProductId(),
                                oldQty - newQty)
                                .then(repo.save(order));

                    }else{
                        return productService.getProductTotalPriceById(
                                order.getProductId(),
                                newQty - oldQty
                                )
                                .flatMap(extraPrice -> {
                                    order.setTotalPrice(order.getTotalPrice() + extraPrice.getTotalPrice());
                                    return repo.save(order);
                                });
                    }
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found with id: " + dto.getOrderId())));
    }

    // Delete order by id
    /* Steps -
    * 1. Find the order by id from DB
    * 2. Delete the order by id and add back the stock for the product
    * 3. Call Customer Service to delete the order from the customer as well.
    * 4. If the HTTP call fails, remove the stock of the product, that has been added (Rollback)
    * 5. Else, send proper response
    * */
    public Mono<ResponseEntity<Void>> deleteOrderById(Integer id){
        return repo.findById(id)
                .flatMap(order -> {
                    return repo.deleteById(id)
                            .then(productService.addStock(order.getProductId(), order.getQuantity()))
                            .then(webClient.build()
                                    .put()
                                    .uri("http://customerservice/customers/remove/order/" + id)
                                    .retrieve()
                                    .onStatus(HttpStatusCode::isError, response ->
                                            Mono.error(new RuntimeException("Failed to remove order from customer"))
                                    )
                                    .bodyToMono(Void.class)
                                    .onErrorResume(ex ->{   // Rollback
                                            productService.removeStock(order.getProductId(), order.getQuantity());
                                            return Mono.error(ex);
                                    })
                            )
                            .thenReturn(ResponseEntity.noContent().<Void>build());
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Order not found with id: " + id)));
    }


}
