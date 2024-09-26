package com.hotel.reservation.auth_service.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
		info = @io.swagger.v3.oas.annotations.info.Info(title = "Authentication Service",
				version = "1.0",
				description = "Documentation Customer Service v1.0"),
		servers = @io.swagger.v3.oas.annotations.servers.Server(url = "http://localhost:8080")
)
@Configuration
public class SwaggerConfig {

	@Value("${spring.application.name}")
	String appName;

	@Bean
	public OpenAPI api() {
		return new OpenAPI().info(new Info().title(appName).description(appName).version("1.0.0"))
			.addServersItem(new Server().url("/"));
	}

}