package com.krasuski.industries.apigateway.handler;

import com.krasuski.industries.apigateway.service.AuthService;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class OrdersServiceHandler {
    private static final String ORDERS_MICROSERVICE_URL = "http://localhost:8082";

    private final AuthService authService;

    public OrdersServiceHandler(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public RouteLocator publicOrdersRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/order/create")
                        .filters(f -> f.setPath("/ddd/order"))
                        .uri(ORDERS_MICROSERVICE_URL))
                .build();
    }

    @Bean
    public RouteLocator securedOrdersRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/order/history")
                        .filters(f -> f.setPath("/order/history")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader()))
                        .uri(ORDERS_MICROSERVICE_URL))
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/order/verification")
                        .filters(f -> f.setPath("/order/verification")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader()))
                        .uri(ORDERS_MICROSERVICE_URL))
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/order/{id}")
                        .filters(f -> f.setPath("/order/{id}")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader()))
                        .uri(ORDERS_MICROSERVICE_URL))
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/order/invoice")
                        .filters(f -> f.setPath("/order/invoice")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader()))
                        .uri(ORDERS_MICROSERVICE_URL))
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/order")
                        .filters(f -> f.setPath("/order")
                                .filter(authService.authenticateRequestAndAppendUserIdHeader()))
                        .uri(ORDERS_MICROSERVICE_URL))
                .build();
    }

    @Bean
    public RouteLocator deliveryRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/delivery/methods")
                        .filters(f -> f.setPath("/delivery/methods"))
                        .uri(ORDERS_MICROSERVICE_URL))
                .build();
    }
}
