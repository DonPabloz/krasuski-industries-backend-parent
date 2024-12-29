package com.krasuski.industries.backend.dto.user.response;


import lombok.Data;

@Data
public class AuthenticateUserResponse {

    private final String jwt;
    private final String username;
}
