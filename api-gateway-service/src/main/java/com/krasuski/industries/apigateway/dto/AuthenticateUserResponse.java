package com.krasuski.industries.apigateway.dto;

public record AuthenticateUserResponse(String accessToken,
                                       String refreshToken,
                                       String userId,
                                       String role) {
}
