package com.krasuski.industries.backend.dto.user.request;

import lombok.Data;

@Data
public class ValidateEmailRequest {
    private Long id;
    private String validationToken;
}
