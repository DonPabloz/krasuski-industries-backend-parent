package com.krasuski.industries.apigateway.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class PaymentServiceHandler {

    @Value("${url.base.payment}")
    private String paymentServiceUrl;

    @Bean
    public RouteLocator publicPaymentRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/payment/methods")
                        .filters(f -> f.setPath("/payment/methods"))
                        .uri(paymentServiceUrl))
                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/payment/payu/notifications")
                        .filters(f -> f.setPath("/payment/payu/notifications"))
                        .uri(paymentServiceUrl))
                .build();
    }
}
