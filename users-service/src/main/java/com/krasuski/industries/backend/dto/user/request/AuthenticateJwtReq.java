package com.krasuski.industries.backend.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthenticateJwtReq {

    @NotBlank(message = "JWT is mandatory")
    private String jwt;
    @NotBlank(message = "Refresh token is mandatory")
    private String refreshToken;
}
