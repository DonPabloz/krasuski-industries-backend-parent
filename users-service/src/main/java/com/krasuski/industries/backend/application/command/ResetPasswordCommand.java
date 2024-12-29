package com.krasuski.industries.backend.application.command;

import com.krasuski.industries.backend.domain.value.Password;
import com.krasuski.industries.backend.domain.value.UserPubId;

public record ResetPasswordCommand(
        UserPubId userPubId,
        Password oldPassword,
        Password newPassword,
        Password repeatedNewPassword
) {
}
