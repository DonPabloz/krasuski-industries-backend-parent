package com.krasuski.industries.apigateway.handler;

import com.krasuski.industries.apigateway.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class CartServiceHandler {

    private final AuthService authService;

    @Value("${url.base.cart}")
    private String cartServiceBaseUrl;

    public CartServiceHandler(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public RouteLocator publicCartRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/cart")
                        .filters(f -> f
                                .setPath("/cart")
                                .filter(authService.optionallyAppendUserIdHeader()))
                        .uri(cartServiceBaseUrl))
                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/cart/product")
                        .uri(cartServiceBaseUrl))
                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/cart/print")
                        .uri(cartServiceBaseUrl))
                .build();
    }
}
