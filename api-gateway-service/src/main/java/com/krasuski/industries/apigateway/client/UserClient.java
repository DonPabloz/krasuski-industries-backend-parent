package com.krasuski.industries.apigateway.client;

import com.krasuski.industries.apigateway.dto.AuthenticateUserResponse;
import reactor.core.publisher.Mono;

public interface UserClient {
    Mono<AuthenticateUserResponse> sendAuthUserReq(String jwt, String refreshToken);
}
