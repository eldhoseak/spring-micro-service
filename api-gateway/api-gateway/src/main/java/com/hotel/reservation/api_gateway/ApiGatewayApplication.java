package com.hotel.reservation.api_gateway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(
		info = @Info(title = "API Gateway",
				version = "1.0",
				description = "Documentation API Gateway v1.0"),
		security = @SecurityRequirement(name = "bearerAuth"),
		servers = @Server(url = "http://localhost:8080")
)
@SecurityScheme(
		name = "bearerAuth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT"
)
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

	@Bean
	public RouteLocator routeLocator(RouteLocatorBuilder builder) {
		return builder
				.routes()
				.route(r -> r.path("/payment-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://PAYMENT-SERVICE"))
				.route(r -> r.path("/auth-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://AUTH-SERVICE"))
				.route(r -> r.path("/customer-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://CUSTOMER-SERVICE"))
				.route(r -> r.path("/hotel-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://HOTEL-SERVICE"))
				.route(r -> r.path("/reservation-service/v3/api-docs").and().method(HttpMethod.GET).uri("lb://RESERVATION-SERVICE"))
				.build();
	}

}
