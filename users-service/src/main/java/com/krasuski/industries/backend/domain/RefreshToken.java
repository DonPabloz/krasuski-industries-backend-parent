package com.krasuski.industries.backend.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Getter
@Setter
public class RefreshToken {

    private final Long userId;
    private final String token;
    private final LocalDateTime creationDate;
    private final Long timeToLiveInSeconds;

    private Boolean isActive;

    public RefreshToken(Long userId, String token, LocalDateTime creationDate, Long timeToLiveInSeconds) {
        this.userId = userId;
        this.token = token;
        this.creationDate = creationDate;
        this.timeToLiveInSeconds = timeToLiveInSeconds;
    }

    public static RefreshToken create(long userId, long timeToLive) {
        return new RefreshToken(userId, generateToken(), LocalDateTime.now(), timeToLive);
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public boolean isValid() {
        if (this.isActive != null && !this.isActive) {
            log.info("Refresh token is not active.");
            return false;
        }
        return creationDate.plusSeconds(timeToLiveInSeconds).isAfter(LocalDateTime.now());
    }

    public void invalidate() {
        isActive = false;
    }
}
