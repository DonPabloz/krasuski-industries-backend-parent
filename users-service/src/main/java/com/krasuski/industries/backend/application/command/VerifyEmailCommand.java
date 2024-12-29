package com.krasuski.industries.backend.application.command;

public record VerifyEmailCommand(
        String token
) {
}
