package com.krasuski.industries.backend.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {

    private String companyName;
    private String companyNip;
    @NotNull(message = "Email can't be null")
    @Email
    private String email;
    @NotNull(message = "Password can't be null")
    @Size(min = 2, max = 20, message = "Password must be equal or grater than 2 characters and less than 20 characters")
    private String password;
    private String phoneNumber;
    private String name;
    private String surname;
}
