package com.krasuski.industries.apigateway.client;

import com.krasuski.industries.apigateway.dto.AuthenticateJwtReq;
import com.krasuski.industries.apigateway.dto.AuthenticateUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Service
public class UserClientImpl implements UserClient {

    private final WebClient webClient;

    public UserClientImpl(WebClient.Builder webClientBuilder, @Value("${url.base.user}") String userServiceBaseUrl) {
        this.webClient = webClientBuilder.baseUrl(userServiceBaseUrl).build();
    }

    @Override
    public Mono<AuthenticateUserResponse> sendAuthUserReq(String jwt, String refreshToken) {
        AuthenticateJwtReq req = new AuthenticateJwtReq(jwt, refreshToken);

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/jwt")
                        .build())
                .body(Mono.just(req), AuthenticateJwtReq.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED)))
                .bodyToMono(new ParameterizedTypeReference<>() {
                });
    }
}
