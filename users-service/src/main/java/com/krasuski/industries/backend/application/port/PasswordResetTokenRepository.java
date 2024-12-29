package com.krasuski.industries.backend.application.port;

import com.krasuski.industries.backend.domain.PasswordResetToken;
import com.krasuski.industries.backend.domain.value.PasswordResetTokenValue;

import java.util.Optional;

public interface PasswordResetTokenRepository {
    void save(PasswordResetToken passwordResetToken);

    Optional<PasswordResetToken> findByToken(PasswordResetTokenValue passwordResetTokenValue);
}
