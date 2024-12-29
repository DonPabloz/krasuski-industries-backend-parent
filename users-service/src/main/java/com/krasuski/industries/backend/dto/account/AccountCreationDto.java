package com.krasuski.industries.backend.dto.account;

import lombok.Value;

@Value
public class AccountCreationDto {
    String recipientAddress;
    String verificationLink;
}
