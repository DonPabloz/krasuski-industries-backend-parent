package com.krasuski.industries.apigateway.handler;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class ProductsServiceHandler {

    private static final String productsMicroserviceUri = "http://localhost:8080";

    @Bean
    public RouteLocator productsRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/product/bargain")
                        .filters(f -> f.setPath("/product/bargain"))
                        .uri(productsMicroserviceUri))
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/product/categories/categories")
                        .filters(f -> f.setPath("/product/categories/categories"))
                        .uri(productsMicroserviceUri))
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/product/categories/info")
                        .filters(f -> f.setPath("/product/categories/info"))
                        .uri(productsMicroserviceUri))
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/product/categories/info/flat")
                        .filters(f -> f.setPath("/product/categories/info/flat"))
                        .uri(productsMicroserviceUri))
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/product/categories/details")
                        .filters(f -> f.setPath("/product/categories/details"))
                        .uri(productsMicroserviceUri))
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/product/list")
                        .filters(f -> f.setPath("/product/list"))
                        .uri(productsMicroserviceUri))
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/product/details")
                        .filters(f -> f.setPath("/product/details"))
                        .uri(productsMicroserviceUri))
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/product/popular")
                        .filters(f -> f.setPath("/product/popular"))
                        .uri(productsMicroserviceUri))
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/search/quick")
                        .filters(f -> f.setPath("/search/quick"))
                        .uri(productsMicroserviceUri))
                .build();
    }
}
