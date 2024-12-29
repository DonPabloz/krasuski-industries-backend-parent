package com.krasuski.industries.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.HttpCookie;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter implements GatewayFilter {

    private final WebClient webClient;

    public AuthenticationFilter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8084").build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpCookie accessTokenCookie = getCookieFromRequest(exchange, "access_token");
        HttpCookie refreshTokenCookie = getCookieFromRequest(exchange, "refresh_token");

        if (accessTokenCookie != null || refreshTokenCookie != null) {
            return webClient.get()
                    .uri("/user/profile")
                    .cookie("access_token", accessTokenCookie.getValue())
                    .cookie("refresh_token", refreshTokenCookie.getValue())
                    .exchange()
                    .flatMap(response -> {
                        if (response.statusCode().is2xxSuccessful()) {
                            return chain.filter(exchange);
                        } else {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }
                    });
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private static HttpCookie getCookieFromRequest(ServerWebExchange exchange, String refresh_token) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String cookies = headers.get("Cookie").get(0);
        List<String> cookiesList = Arrays.stream(cookies.split("; ")).toList();

        return cookiesList.stream()
                .map(ca -> HttpCookie.parse(ca).get(0))
                .filter(c -> refresh_token.equals(c.getName()))
                .findFirst()
                .orElse(null);
    }
}

