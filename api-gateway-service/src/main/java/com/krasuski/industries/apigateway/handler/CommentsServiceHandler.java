package com.krasuski.industries.apigateway.handler;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class CommentsServiceHandler {

    private static final String commentMicroserviceUri = "http://localhost:8081";

    @Bean
    public RouteLocator commentsRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/comment/rated")
                        .filters(f -> f.setPath("/rated/comment"))
                        .uri(commentMicroserviceUri))
                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/comment/rated")
                        .filters(f -> f.setPath("/add/rated/comment"))
                        .uri(commentMicroserviceUri))
                .build();
    }
}
