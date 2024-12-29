package com.krasuski.industries.backend.domain;

import com.krasuski.industries.backend.domain.value.PasswordResetTokenValue;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class PasswordResetToken {

    private User user;
    private LocalDateTime creationDate;
    private PasswordResetTokenValue token;
    private LocalDateTime expirationDate;

    public static PasswordResetToken create(User user, long expirationTimeInMinutes) {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(user);
        passwordResetToken.setToken(passwordResetToken.generateToken());
        passwordResetToken.setCreationDate(LocalDateTime.now());
        passwordResetToken.setExpirationDate(passwordResetToken.getCreationDate().plusMinutes(expirationTimeInMinutes));

        return passwordResetToken;
    }

    public boolean isTokenValid(PasswordResetTokenValue token) {
        return this.token.equals(token) && LocalDateTime.now().isBefore(expirationDate);
    }

    private PasswordResetTokenValue generateToken() {
        return new PasswordResetTokenValue(UUID.randomUUID().toString());
    }
}
