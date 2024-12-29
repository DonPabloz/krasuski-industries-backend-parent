package com.krasuski.industries.apigateway.service;

import com.krasuski.industries.apigateway.client.UserClient;
import com.krasuski.industries.apigateway.dto.AuthenticateUserResponse;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String USER_ID_HEADER_NAME = "X-User-Id";
    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    private static final String ADMIN_ROLE = "admin";

    private final UserClient userClient;

    public AuthServiceImpl(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public GatewayFilter optionallyAppendUserIdHeader() {
        return (exchange, chain) -> {
            HttpCookie accessTokenCookie = getAccessTokenCookie(exchange);
            if (accessTokenCookie == null) {
                return chain.filter(exchange);
            }

            HttpCookie refreshToken = getRefreshTokenCookie(exchange);
            if (refreshToken == null) {
                return chain.filter(exchange);
            }

            return validateAccessToken(accessTokenCookie, refreshToken, exchange.getResponse())
                    .map(AuthenticateUserResponse::userId)
                    .flatMap(userId -> prepareResponse(exchange, chain, userId));
        };
    }

    @Override
    public GatewayFilter authenticateRequestAndAppendUserIdHeader() {
        return (exchange, chain) -> {
            HttpCookie accessTokenCookie = getAccessTokenCookie(exchange);
            if (accessTokenCookie == null) {
                return returnUnauthorizedResponse(exchange);
            }

            HttpCookie refreshToken = getRefreshTokenCookie(exchange);
            if (refreshToken == null) {
                return returnUnauthorizedResponse(exchange);
            }

            return validateAccessToken(accessTokenCookie, refreshToken, exchange.getResponse())
                    .map(AuthenticateUserResponse::userId)
                    .flatMap(userId -> prepareResponse(exchange, chain, userId));
        };
    }

    @Override
    public GatewayFilter authenticateAdmin() {
        return (exchange, chain) -> {
            HttpCookie accessTokenCookie = getAccessTokenCookie(exchange);
            if (accessTokenCookie == null) {
                return returnUnauthorizedResponse(exchange);
            }

            HttpCookie refreshToken = getRefreshTokenCookie(exchange);
            if (refreshToken == null) {
                return returnUnauthorizedResponse(exchange);
            }

            return validateAccessToken(accessTokenCookie, refreshToken, exchange.getResponse())
                    .filter(authenticateUserResponse -> authenticateUserResponse.role().equalsIgnoreCase(ADMIN_ROLE))
                    .map(resp -> Optional.of(resp.userId()))
                    .defaultIfEmpty(Optional.empty())
                    .flatMap(userId -> userId.isPresent() ? prepareResponse(exchange, chain, userId.get()) : returnUnauthorizedResponse(exchange));
        };
    }

    private HttpCookie getAccessTokenCookie(ServerWebExchange exchange) {
        return exchange.getRequest().getCookies().getFirst(ACCESS_TOKEN_COOKIE_NAME);
    }

    private HttpCookie getRefreshTokenCookie(ServerWebExchange exchange) {
        return exchange.getRequest().getCookies().getFirst(REFRESH_TOKEN_COOKIE_NAME);
    }

    private Mono<Void> returnUnauthorizedResponse(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);

        return response.setComplete();
    }

    private Mono<AuthenticateUserResponse> validateAccessToken(HttpCookie accessTokenCookie, HttpCookie refreshTokenCookie, ServerHttpResponse response) {
        return userClient.sendAuthUserReq(accessTokenCookie.getValue(), refreshTokenCookie.getValue())
                .map(authenticateUserResponse -> {
                    ResponseCookie updatedAccessTokenCookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE_NAME, authenticateUserResponse.accessToken())
                            .path("/")
                            .secure(false)
                            .httpOnly(true)
                            .build();
                    ResponseCookie updatedRefreshTokenCookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE_NAME, authenticateUserResponse.refreshToken())
                            .path("/")
                            .secure(false)
                            .httpOnly(true)
                            .build();
                    response.addCookie(updatedAccessTokenCookie);
                    response.addCookie(updatedRefreshTokenCookie);

                    return authenticateUserResponse;
                });
    }

    private Mono<? extends Void> prepareResponse(ServerWebExchange exchange, GatewayFilterChain chain, String userId) {
        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                .header(USER_ID_HEADER_NAME, userId)
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }
}
