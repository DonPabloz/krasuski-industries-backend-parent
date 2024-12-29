package com.krasuski.industries.backend.application.command;

import com.krasuski.industries.backend.domain.value.Password;
import com.krasuski.industries.backend.domain.value.PasswordResetTokenValue;

public record ResetPasswordByTokenCommand(
        PasswordResetTokenValue passwordResetTokenValue,
        Password newPassword
) {
}
