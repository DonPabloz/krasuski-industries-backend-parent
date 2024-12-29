package com.krasuski.industries.apigateway.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Configuration
public class WebFilterImpl implements WebFilter {

    private static final String X_CORRELATION_ID_HEADER_NAME = "X-CORRELATION-ID";

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        String correlationId = UUID.randomUUID().toString();
        serverWebExchange.getRequest().mutate().header(X_CORRELATION_ID_HEADER_NAME, correlationId).build();

        return webFilterChain.filter(serverWebExchange);
    }
}
