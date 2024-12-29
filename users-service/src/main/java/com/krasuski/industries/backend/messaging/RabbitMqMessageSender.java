package com.krasuski.industries.backend.messaging;

import com.krasuski.industries.backend.dto.account.AccountCreationDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqMessageSender implements MessageSender {

    private static final String ACCOUNT_CREATION_VALIDATION_EMAIL_QUEUE = "accountCreationValidationEmail";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendAccountCreationValidationEmail(AccountCreationDto accountCreationDto) {
        rabbitTemplate.convertAndSend(ACCOUNT_CREATION_VALIDATION_EMAIL_QUEUE, accountCreationDto);
    }
}
