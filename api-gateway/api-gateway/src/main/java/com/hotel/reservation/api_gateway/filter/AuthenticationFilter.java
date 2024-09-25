package com.hotel.reservation.api_gateway.filter;

import com.hotel.reservation.api_gateway.service.JwtUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Slf4j
@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    RouteValidator routeValidator;

    @Autowired
    JwtUtil jwtUtil;

    public static class Config {
        // Add configuration properties here if needed
    }

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            if(routeValidator.isSecured.test(request)){
                if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    return Mono.defer(() -> {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
                }
                String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if(authHeader != null && authHeader.startsWith("Bearer ")){
                    authHeader = authHeader.substring(7);
                }
                    if(!jwtUtil.validateToken(authHeader)){
                        return Mono.defer(() -> {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        });
                    }
            }
            return chain.filter(exchange);
        };
    }

}