package com.practice.apigateway.service;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class RouteValidatorService {
    private final List<String> openRoutes = List.of(
            "/auth/register",
            "/auth/login",
            "/auth/validate"
    );

    public final Predicate<ServerHttpRequest> isRouteSecured = request ->
            openRoutes.stream()
                    .noneMatch(route -> request.getURI().getPath().contains(route));
}
