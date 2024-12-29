package com.krasuski.industries.apigateway.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class ReadModelServiceHandler {

    @Value("${url.base.readModel}")
    private String readModelMicroserviceUri;

    @Bean
    public RouteLocator readModelRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/print/calculated")
                        .filters(f -> f.setPath("/print/calculated"))
                        .uri(readModelMicroserviceUri))
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/payment/{orderId}")
                        .filters(f -> f.setPath("/payment/{orderId}"))
                        .uri(readModelMicroserviceUri))
                .build();
    }
}
