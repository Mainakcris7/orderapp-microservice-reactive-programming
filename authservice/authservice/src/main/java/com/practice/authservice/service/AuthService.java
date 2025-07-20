package com.practice.authservice.service;

import com.practice.authservice.dto.CustomerLoginDto;
import com.practice.authservice.dto.CustomerRegisterDto;
import com.practice.authservice.dto.CustomerUpdateDto;
import com.practice.authservice.dto.JwtDto;
import com.practice.authservice.model.Customer;
import com.practice.authservice.repository.CustomerRepository;
import com.practice.authservice.security.JwtUtils;
import com.practice.authservice.util.CustomerMapperUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class AuthService {
    private final CustomerMapperUtil mapperUtil;
    private final CustomerRepository repo;
    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    // Save a new customer
    public Mono<ResponseEntity<Customer>> saveCustomer(CustomerRegisterDto dto){
        Customer customer = mapperUtil.fromRegisterDto(dto);

        return repo.save(customer)
                .map(c -> ResponseEntity.status(HttpStatus.CREATED).body(c))
                .switchIfEmpty(Mono.error(new RuntimeException("Failed to save customer")));
    }

    // Update the details of a customer
    public Mono<Customer> updateCustomer(CustomerUpdateDto dto){
        Customer customer = mapperUtil.fromUpdateDto(dto);

        return repo.existsById(customer.getCustomerId())
                .flatMap(exists -> {
                    if(exists)
                        return repo.save(customer)
                                .switchIfEmpty(Mono.error(new RuntimeException("Failed to update customer")));
                    else
                        return Mono.error(new RuntimeException("No customer found with id: " + customer.getCustomerId()));
                });
    }

    // Login a customer and generate Jwt Token
    public Mono<JwtDto> loginCustomer(CustomerLoginDto dto) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());

        return authenticationManager.authenticate(token)
                .flatMap(auth -> {
                    if(auth.isAuthenticated()){
                        String jwtToken = jwtUtils.generateJWTToken(dto.getEmail());
                        JwtDto jwtDto = new JwtDto(jwtToken);
                        return Mono.just(jwtDto);
                    }else{
                        return Mono.error(new RuntimeException("Access Denied!"));
                    }
                });
    }

    // Validate the Jwt Token
    public Mono<Boolean> validateJwtToken(JwtDto jwtDto) {
        if(jwtUtils.validateToken(jwtDto.getJwtToken())){
            return Mono.just(true);
        }else{
            return Mono.error(new RuntimeException("Invalid Jwt Token!"));
        }
    }
}
