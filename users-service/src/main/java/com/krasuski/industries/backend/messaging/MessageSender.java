package com.krasuski.industries.backend.messaging;

import com.krasuski.industries.backend.dto.account.AccountCreationDto;

public interface MessageSender {

    void sendAccountCreationValidationEmail(AccountCreationDto accountCreationDto);
}
