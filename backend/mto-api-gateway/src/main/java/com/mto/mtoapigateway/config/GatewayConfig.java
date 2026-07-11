package com.mto.mtoapigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(
            RouteLocatorBuilder builder) {

        return builder.routes()

                .route("auth-service",
                        r -> r.path("/api/v1/auth/**")
                                .uri("http://localhost:8081"))

                .route("user-service",
                        r -> r.path("/users/**")
                                .uri("http://localhost:8081"))

                .route("order-service",
                        r -> r.path("/test/**")
                                .uri("http://localhost:8080"))

                .build();
    }
}