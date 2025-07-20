package com.practice.apigateway.filter;

import com.practice.apigateway.service.JwtValidatorService;
import com.practice.apigateway.service.RouteValidatorService;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final JwtValidatorService jwtValidatorService;
    private final RouteValidatorService routeValidatorService;

    public AuthFilter(JwtValidatorService jwtValidatorService, RouteValidatorService routeValidatorService){
        super(Config.class);
        this.jwtValidatorService = jwtValidatorService;
        this.routeValidatorService = routeValidatorService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return((exchange, chain) -> {
            // Check whether the accessed route/path is secured
            if(routeValidatorService.isRouteSecured.test(exchange.getRequest())){

                // Check whether the secured route contains Authorization header
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new RuntimeException("Missing Authorization Header!");
                }

                // Fetch the header
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).getFirst();

                // Check whether the client has sent the Jwt in the format Bearer <Jwt>
                if(authHeader != null && authHeader.startsWith("Bearer")){
                    // Fetch the Jwt Token
                    String jwtToken = authHeader.substring(7);

                    // Validate it through Auth Service
                    return jwtValidatorService.validateJwtToken(jwtToken)
                            .flatMap(res -> {
                                if(!Boolean.TRUE.equals(res)){
                                    return Mono.error(new RuntimeException("Invalid Jwt Token!"));
                                }
                                return chain.filter(exchange);
                            });
                }
            }
            return chain.filter(exchange);
        });
    }

    public static class Config{

    }
}
