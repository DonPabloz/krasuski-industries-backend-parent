package com.krasuski.industries.backend.dto.user.request;

import lombok.Data;

@Data
public class AuthenticateUserRequest {

    private String mail;
    private String password;
}
