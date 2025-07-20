package com.practice.customerservice.service;

import com.practice.customerservice.dto.AddCustomerOrderDto;
import com.practice.customerservice.dto.NotificationDto;
import com.practice.customerservice.model.Customer;
import com.practice.customerservice.model.CustomerOrder;
import com.practice.customerservice.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository repo;
    private final CustomerOrderService customerOrderService;
    private final WebClient.Builder webClient;
    private final NotifierService notifierService;

    // Get all customers
    public Flux<Customer> getAllCustomers(){
        return repo.findAll()
                .switchIfEmpty(Mono.error(new RuntimeException("No customers found!")));
    }

    // Get customer by id
    public Mono<Customer> getCustomerById(Integer id){
        return repo.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("No customer found with id: " + id)));
    }

    // Save a new customer
    public Mono<ResponseEntity<Customer>> saveCustomer(Customer customer){
        customer.setCustomerId(null);
        return repo.save(customer)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c))
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to save customer")));
    }

    // Update the details of a customer
    public Mono<Customer> updateCustomer(Customer customer){
        return repo.existsById(customer.getCustomerId())
                .flatMap(exists -> {
                    if(exists)
                        return repo.save(customer)
                                .switchIfEmpty(Mono.error(new RuntimeException("Failed to update customer")));
                    else
                        return Mono.error(new RuntimeException("No customer found with id: " + customer.getCustomerId()));
                });
    }

    // Add an order to an existing customer
    /* Steps -
    * 1. Save the association of the order with the customer in customer_orders table
    * 2. Fetch the details of the customer by id
    * 3. Create the notification DTO and fill the details
    * 4. Send the notification using RabbitMQ
    * */
    public Mono<CustomerOrder> addOrderToCustomer(AddCustomerOrderDto dto){
        return customerOrderService.addCustomerOrder(dto.getCustomerId(), dto.getOrderId())
                .flatMap(order -> {
                    return getCustomerById(dto.getCustomerId())
                            .map(customer -> {
                                NotificationDto notificationDto = new NotificationDto();

                                notificationDto.setCustomerName(customer.getName());
                                notificationDto.setEmail(customer.getEmail());
                                notificationDto.setTotalPrice(dto.getTotalPrice());
                                notificationDto.setProductName(dto.getProductName());
                                notificationDto.setProductQuantity(dto.getQuantity());
                                notificationDto.setOrderType("ORDER_CREATED");

                                notifierService.notificationSender(notificationDto);

                                return order;
                            });
                });
    }

    // Remove an order from a customer
    public Mono<Void> removeOrderFromCustomer(Integer orderId){
        return customerOrderService.removeCustomerOrder(orderId);
    }

    // Delete a customer by id
    /* Steps -
    * 1. Check & fetch the customer details by id
    * 2. Fetch all the orders associated with it
    * 3. Call order service to delete those orders
    * 4. Delete the orders from the customer_orders table (Will be done from order service)
    * 5. Delete the customer by id
    * */
    public Mono<ResponseEntity<Void>> deleteCustomerById(Integer id){
        return repo.existsById(id)
                .flatMap(exists -> {
                    if(exists){
                        return customerOrderService.getOrdersByCustomerId(id)
                                .flatMap(order -> webClient.build()
                                        .delete()
                                        .uri("http://orderservice/orders/"+order.getOrderId())
                                        .retrieve()
                                        .onStatus(HttpStatusCode::isError, response ->
                                            Mono.error(new RuntimeException("Failed to delete order with id: " + order.getOrderId()))
                                        )
                                        .bodyToMono(Void.class)
                                )
                                .then()
                                .then(repo.deleteById(id))
                                .thenReturn(ResponseEntity.noContent().build());

                    }else{
                        return Mono.error(new RuntimeException("No customer found with id: " + id));
                    }
                });
    }
}
