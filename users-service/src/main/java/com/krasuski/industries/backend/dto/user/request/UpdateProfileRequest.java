package com.krasuski.industries.backend.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    private String companyName;
    private String companyNip;
    @Email(message = "Email should be valid")
    private String email;
    private String name;
    private String surname;
    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;
}
