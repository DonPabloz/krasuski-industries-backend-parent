package com.krasuski.industries.backend.application.command;

import com.krasuski.industries.backend.domain.value.Email;
import com.krasuski.industries.backend.domain.value.Password;

public record BasicAuthenticationCommand(
        Email mail,
        Password password
) {
}
