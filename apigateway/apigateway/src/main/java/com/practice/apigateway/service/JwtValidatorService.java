package com.practice.apigateway.service;

import com.practice.apigateway.dto.JwtDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class JwtValidatorService {

    private final WebClient webClient;

    public JwtValidatorService(WebClient.Builder webClient){
        this.webClient = webClient.build();
    }

    public Mono<Boolean> validateJwtToken(String jwtToken){
        return webClient.post()
                .uri("http://AUTHSERVICE/auth/validate")
                .bodyValue(new JwtDto(jwtToken))
                .retrieve()
                .bodyToMono(Boolean.class);
    }
}
