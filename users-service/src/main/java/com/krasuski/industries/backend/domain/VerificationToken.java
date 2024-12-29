package com.krasuski.industries.backend.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class VerificationToken {

    private User user;
    private String token;
    private LocalDateTime creationDate;
    private LocalDateTime expirationDate;

    public VerificationToken(User user, long expirationTimeInMinutes) {
        this.user = user;
        this.token = generateToken();
        this.creationDate = LocalDateTime.now();
        this.expirationDate = this.creationDate.plusMinutes(expirationTimeInMinutes);
    }

    public static VerificationToken createNew(User user, long expirationTimeInMinutes) {
        return new VerificationToken(user, expirationTimeInMinutes);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public boolean isExpired() {
        return expirationDate.isBefore(LocalDateTime.now());
    }
}
