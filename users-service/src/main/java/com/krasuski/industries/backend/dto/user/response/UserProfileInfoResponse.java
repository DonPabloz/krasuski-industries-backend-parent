package com.krasuski.industries.backend.dto.user.response;

import lombok.Data;

@Data
public class UserProfileInfoResponse {

    private String companyName;
    private String companyNip;
    private String email;
    private String name;
    private String surname;
    private String phoneExtension;
    private String phoneNumber;
}
