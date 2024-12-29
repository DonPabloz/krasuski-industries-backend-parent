package com.krasuski.industries.apigateway.handler;

import com.krasuski.industries.apigateway.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class CommunicationServiceHandler {

    private final AuthService authService;

    @Value("${url.base.communication}")
    private String communicationServiceBaseUrl;

    public CommunicationServiceHandler(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public RouteLocator publicCommunicationRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .method(HttpMethod.POST)
                        .and()
                        .path("/message")
                        .filters(f -> f.setPath("/message"))
                        .uri(communicationServiceBaseUrl))
                .build();
    }

    @Bean
    public RouteLocator securedCommunicationRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r
                        .method(HttpMethod.GET)
                        .and()
                        .path("/message")
                        .filters(f -> f
                                .filter(authService.authenticateAdmin())
                                .setPath("/message"))
                        .uri(communicationServiceBaseUrl))
                .build();
    }
}
