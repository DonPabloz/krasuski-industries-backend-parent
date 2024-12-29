package com.krasuski.industries.backend.service;

import com.krasuski.industries.backend.AbstractUnitTest;
import com.krasuski.industries.backend.entity.UserEntity;
import com.krasuski.industries.backend.entity.UserRefreshToken;
import com.krasuski.industries.backend.repositories.user.UserRefreshTokenRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

class RefreshTokenServiceTest extends AbstractUnitTest {

    private static final int refreshTokenTimeToLive = 100;
    private static final UserRefreshToken VALID_USER_REFRESH_TOKEN_1 = new UserRefreshToken();
    private static final UserRefreshToken VALID_USER_REFRESH_TOKEN_2 = new UserRefreshToken();
    private static final UserRefreshToken EXPIRED_USER_REFRESH_TOKEN_1 = new UserRefreshToken();
    private static final UserRefreshToken EXPIRED_USER_REFRESH_TOKEN_2 = new UserRefreshToken();

    @Mock
    private UserRefreshTokenRepository userRefreshTokenRepository;

    private RefreshTokenService refreshTokenService;

    @BeforeAll
    static void setUpAll() {
        VALID_USER_REFRESH_TOKEN_1.setToken("token1");
        VALID_USER_REFRESH_TOKEN_1.setExpirationTime(System.currentTimeMillis() + 100000);

        VALID_USER_REFRESH_TOKEN_2.setToken("token2");
        VALID_USER_REFRESH_TOKEN_2.setExpirationTime(System.currentTimeMillis() + 100000);

        EXPIRED_USER_REFRESH_TOKEN_1.setToken("token3");
        EXPIRED_USER_REFRESH_TOKEN_1.setExpirationTime(System.currentTimeMillis() - 100000);

        EXPIRED_USER_REFRESH_TOKEN_2.setToken("token4");
        EXPIRED_USER_REFRESH_TOKEN_2.setExpirationTime(System.currentTimeMillis() - 100000);
    }

    @BeforeEach
    void setUp() {
        refreshTokenService = new RefreshTokenService(userRefreshTokenRepository, refreshTokenTimeToLive);
    }

    @Test
    void givenNullUser_whenCreatingRefreshToken_thenThrowException() {
        // given
        // when
        // then
        assertThrows(ResponseStatusException.class, () -> refreshTokenService.createAndSaveRefreshToken(null));
    }

    @Test
    void givenProperUser_whenCreatingRefreshToken_thenReturnToken() {
        // given
        UserEntity user = getRegularUser();

        // when
        doReturn(new UserRefreshToken()).when(userRefreshTokenRepository).save(any());
        String token = refreshTokenService.createAndSaveRefreshToken(user);

        // then
        assertNotNull(token);
    }

    @Test
    void givenEmptyRefreshToken_whenCheckingIfValid_thenReturnFalse() {
        // given
        // when
        boolean result = refreshTokenService.isRefreshTokenValid("", 1L);

        // then
        assertFalse(result);
    }

    @Test
    void givenEmptyUserId_whenCheckingIfValid_thenReturnFalse() {
        // given
        // when
        boolean result = refreshTokenService.isRefreshTokenValid("token", null);

        // then
        assertFalse(result);
    }

    @Test
    void givenNoUserRefreshToken_whenCheckingIfValid_thenReturnFalse() {
        // given
        Long userId = 1L;
        doReturn(List.of()).when(userRefreshTokenRepository).findAllByUserEntityId(userId);

        // when
        boolean result = refreshTokenService.isRefreshTokenValid("token", userId);

        // then
        assertFalse(result);
    }

    @Test
    void givenDifferentToken_whenCheckingIfValid_thenReturnFalse() {
        // given
        Long userId = 1L;
        doReturn(List.of(VALID_USER_REFRESH_TOKEN_1)).when(userRefreshTokenRepository).findAllByUserEntityId(userId);

        // when
        boolean result = refreshTokenService.isRefreshTokenValid("differentToken", userId);

        // then
        assertFalse(result);
    }

    @Test
    void givenExpiredToken_whenCheckingIfValid_thenReturnFalse() {
        // given
        Long userId = 1L;
        doReturn(List.of(EXPIRED_USER_REFRESH_TOKEN_1)).when(userRefreshTokenRepository).findAllByUserEntityId(userId);

        // when
        boolean result = refreshTokenService.isRefreshTokenValid(EXPIRED_USER_REFRESH_TOKEN_1.getToken(), userId);

        // then
        assertFalse(result);
    }

    @Test
    void givenValidToken_whenCheckingIfValid_thenReturnTrue() {
        // given
        Long userId = 1L;
        doReturn(List.of(VALID_USER_REFRESH_TOKEN_1)).when(userRefreshTokenRepository).findAllByUserEntityId(userId);

        // when
        boolean result = refreshTokenService.isRefreshTokenValid(VALID_USER_REFRESH_TOKEN_1.getToken(), userId);

        // then
        assertTrue(result);
    }

    @Test
    void givenValidTokenExists_whenCreatingRefreshToken_thenReturnExistingToken() {
        // given
        UserEntity user = getRegularUser();
        doReturn(List.of(VALID_USER_REFRESH_TOKEN_1)).when(userRefreshTokenRepository).findAllByUserEntityId(user.getId());

        // when
        String tokenFromMethod = refreshTokenService.createAndSaveRefreshToken(user);

        // then
        assertEquals(VALID_USER_REFRESH_TOKEN_1.getToken(), tokenFromMethod);
    }

    @Test
    void givenInvalidTokenExists_whenCreatingRefreshToken_thenReturnNewToken() {
        // given
        UserEntity user = getRegularUser();
        doReturn(List.of(EXPIRED_USER_REFRESH_TOKEN_1)).when(userRefreshTokenRepository).findAllByUserEntityId(user.getId());

        // when
        String tokenFromMethod = refreshTokenService.createAndSaveRefreshToken(user);

        // then
        assertNotEquals(EXPIRED_USER_REFRESH_TOKEN_1.getToken(), tokenFromMethod);
    }

    @Test
    void givenMultipleTokensAndOneOfThemIsValid_whenIsRefreshTokenValid_thenReturnTrue() {
        // given
        Long userId = 1L;
        doReturn(List.of(EXPIRED_USER_REFRESH_TOKEN_1, VALID_USER_REFRESH_TOKEN_1)).when(userRefreshTokenRepository).findAllByUserEntityId(userId);

        // when
        boolean result = refreshTokenService.isRefreshTokenValid(VALID_USER_REFRESH_TOKEN_1.getToken(), userId);

        // then
        assertTrue(result);
    }

    @Test
    void givenMultipleTokensAndNoneOfThemIsValid_whenIsRefreshTokenValid_thenReturnFalse() {
        // given
        Long userId = 1L;
        doReturn(List.of(EXPIRED_USER_REFRESH_TOKEN_1, EXPIRED_USER_REFRESH_TOKEN_2)).when(userRefreshTokenRepository).findAllByUserEntityId(userId);

        // when
        boolean result = refreshTokenService.isRefreshTokenValid(EXPIRED_USER_REFRESH_TOKEN_1.getToken(), userId);

        // then
        assertFalse(result);
    }

    @Test
    void givenMultipleTokensAndAllOfThemAreValid_whenIsRefreshTokenValid_thenReturnTrue() {
        // given
        Long userId = 1L;
        doReturn(List.of(VALID_USER_REFRESH_TOKEN_1, VALID_USER_REFRESH_TOKEN_2)).when(userRefreshTokenRepository).findAllByUserEntityId(userId);

        // when
        boolean result = refreshTokenService.isRefreshTokenValid(VALID_USER_REFRESH_TOKEN_1.getToken(), userId);
        boolean result2 = refreshTokenService.isRefreshTokenValid(VALID_USER_REFRESH_TOKEN_2.getToken(), userId);

        // then
        assertTrue(result);
        assertTrue(result2);
    }
}