package com.krasuski.industries.backend.dto.user.request;

public record LogoutUserRequest(
        String userPubId,
        String refreshToken
) {
}
