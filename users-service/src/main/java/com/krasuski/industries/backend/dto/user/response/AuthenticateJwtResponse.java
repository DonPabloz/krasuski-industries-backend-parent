package com.krasuski.industries.backend.dto.user.response;

import lombok.Data;

@Data
public class AuthenticateJwtResponse {
    private String accessToken;
    private String refreshToken;
    private String userId;
    private String role;
}
