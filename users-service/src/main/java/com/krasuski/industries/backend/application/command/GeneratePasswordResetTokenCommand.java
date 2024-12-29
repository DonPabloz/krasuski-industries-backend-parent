package com.krasuski.industries.backend.application.command;

import com.krasuski.industries.backend.domain.value.Email;

public record GeneratePasswordResetTokenCommand(Email email) {
}
