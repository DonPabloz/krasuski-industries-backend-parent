package com.krasuski.industries.backend.dto.user.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String oldPassword;
    private String newPassword;
    private String repeatedNewPassword;
}
