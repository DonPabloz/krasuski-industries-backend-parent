package com.krasuski.industries.apigateway.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class DeliveryServiceHandler {

    @Value("${url.base.delivery}")
    private String paymentServiceUrl;

    @Bean
    public RouteLocator publicDeliveryRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/delivery/method")
                        .filters(f -> f.setPath("/delivery/method"))
                        .uri(paymentServiceUrl))
                .build();
    }
}
