package com.krasuski.industries.apigateway.dto;

public record AuthenticateJwtReq(String jwt, String refreshToken) {
}
