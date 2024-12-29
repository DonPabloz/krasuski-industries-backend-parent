package com.krasuski.industries.apigateway.configuration;

import com.krasuski.industries.apigateway.filter.AuthenticationFilter;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GatewayFilterConfig {

    private final WebClient.Builder webClient;

    public GatewayFilterConfig(WebClient.Builder webClient) {
        this.webClient = webClient;
    }

    @Bean
    public GatewayFilter authenticationFilter() {
        return new AuthenticationFilter(webClient);
    }
}
