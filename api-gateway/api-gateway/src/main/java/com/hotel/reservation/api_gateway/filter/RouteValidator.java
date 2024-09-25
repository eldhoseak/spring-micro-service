package com.hotel.reservation.api_gateway.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {
private static final List<String> endPoints = List.of("/api/auth/**","/eureka");

public Predicate<ServerHttpRequest> isSecured =
        request -> endPoints.stream().noneMatch(request.getURI().getPath()::contains);
}
