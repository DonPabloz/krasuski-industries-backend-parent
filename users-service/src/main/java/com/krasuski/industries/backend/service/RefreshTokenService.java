package com.krasuski.industries.backend.service;

import com.krasuski.industries.backend.entity.UserEntity;
import com.krasuski.industries.backend.entity.UserRefreshToken;
import com.krasuski.industries.backend.repositories.user.UserRefreshTokenRepository;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class RefreshTokenService {

    private final UserRefreshTokenRepository userRefreshTokenRepository;


    private final int refreshTokenTimeToLive;

    public RefreshTokenService(UserRefreshTokenRepository userRefreshTokenRepository,
                               @Value("${refresh-token-time-to-live}") int refreshTokenTimeToLive) {
        this.userRefreshTokenRepository = userRefreshTokenRepository;
        this.refreshTokenTimeToLive = refreshTokenTimeToLive;
    }

    public String createAndSaveRefreshToken(UserEntity user) {
        if (user == null) {
            log.error("User is null.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is null.");
        }

        List<UserRefreshToken> userRefreshTokens = userRefreshTokenRepository.findAllByUserEntityId(user.getId());
        if (userRefreshTokens.isEmpty()) {
            UserRefreshToken newRefreshToken = createNewRefreshToken(user);
            return newRefreshToken.getToken();
        }

        return userRefreshTokens.stream()
                .filter(userRefreshToken -> !isRefreshTokenExpired(userRefreshToken))
                .findAny()
                .orElseGet(() -> createNewRefreshToken(user))
                .getToken();

    }

    public boolean isRefreshTokenValid(String refreshToken, Long userId) {
        if (StringUtils.isBlank(refreshToken)) {
            log.error("Refresh token is empty.");
            return false;
        }

        List<UserRefreshToken> optionalUserRefreshToken = userRefreshTokenRepository.findAllByUserEntityId(userId);
        if (optionalUserRefreshToken.isEmpty()) {
            log.error("User with id '{}' has no refresh tokens.", userId);
            return false;
        }

        Optional<UserRefreshToken> userRefreshTokenOpt = optionalUserRefreshToken.stream()
                .filter(userRefreshToken -> refreshToken.equals(userRefreshToken.getToken()))
                .findAny();

        if (userRefreshTokenOpt.isEmpty()) {
            log.error("User refresh token not found in DB for user with id: {}", userId);
            return false;
        }

        UserRefreshToken userRefreshToken = userRefreshTokenOpt.get();
        if (!refreshToken.equals(userRefreshToken.getToken())) {
            log.error("Refresh token saved in DB is not the same as refresh token from request.");
            return false;
        }

        if (isRefreshTokenExpired(userRefreshToken)) {
            log.error("Refresh token has expired at {}", Instant.ofEpochMilli(userRefreshToken.getExpirationTime()).atZone(ZoneId.systemDefault()).toLocalDateTime());
            return false;
        }

        log.info("Refresh token is valid.");

        return true;
    }

    private UserRefreshToken createNewRefreshToken(UserEntity userEntity) {
        String refreshToken = UUID.randomUUID().toString();
        long expirationTime = System.currentTimeMillis() + refreshTokenTimeToLive;

        UserRefreshToken userRefreshToken = new UserRefreshToken();
        userRefreshToken.userEntity = userEntity;
        userRefreshToken.token = refreshToken;
        userRefreshToken.expirationTime = expirationTime;
        userRefreshTokenRepository.save(userRefreshToken);

        return userRefreshToken;
    }

    private boolean isRefreshTokenExpired(UserRefreshToken userRefreshToken) {
        return userRefreshToken.getExpirationTime() < System.currentTimeMillis();
    }
}
