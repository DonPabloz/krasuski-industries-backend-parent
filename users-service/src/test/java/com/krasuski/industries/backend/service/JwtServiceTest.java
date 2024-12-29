package com.krasuski.industries.backend.service;

import com.krasuski.industries.backend.AbstractUnitTest;
import com.krasuski.industries.backend.enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest extends AbstractUnitTest {
    private static final int jwtTimeToLive = 100;

    private final JwtService jwtService = new JwtService("secret", jwtTimeToLive);

    @Test
    void givenAdminRole_whenGeneratingToken_shouldGenerateTokenWithAdminRole() {
        // given
        Role role = Role.ADMIN;

        // when
        String token = jwtService.generateToken(UUID.randomUUID(), role);

        // then
        assertEquals(role.toString(), jwtService.extractUserRole(token));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "   "})
    void givenEmptyToken_whenCheckingJwtValidity_thenReturnFalse(String token) {
        //given

        //when
        boolean isJwtValid = jwtService.isJwtValid(token);

        //then
        assertFalse(isJwtValid);
    }

    @Test
    void givenExpiredToken_whenCheckingJwtValidity_thenReturnFalse() {
        //given
        String token = jwtService.generateToken(UUID.randomUUID(), Role.ADMIN);
        waitForJwtToExpire();

        //when
        boolean isJwtValid = jwtService.isJwtValid(token);

        //then
        assertFalse(isJwtValid);
    }

    @Test
    void givenInvalidToken_whenCheckingJwtValidity_thenReturnFalse() {
        //given
        String token = "asd.asd.asd";

        //when
        boolean isJwtValid = jwtService.isJwtValid(token);

        //then
        assertFalse(isJwtValid);
    }

    @Test
    void givenMalformedJwt_whenCheckingJwtValidity_thenReturnFalse() {
        //given
        String token = "asd.asd";

        //when
        boolean isJwtValid = jwtService.isJwtValid(token);

        //then
        assertFalse(isJwtValid);
    }

    @Test
    void givenValidToken_whenCheckingJwtValidity_thenReturnTrue() {
        //given
        // This test sometimes takes a moment, so we need to make sure that jwt lives long enough
        JwtService jwtService = new JwtService("secret", 5000);
        String token = jwtService.generateToken(UUID.randomUUID(), Role.ADMIN);

        //when
        boolean isJwtValid = jwtService.isJwtValid(token);

        //then
        assertTrue(isJwtValid);
    }

    @Test
    void givenValidToken_whenExtractingUserPublicId_thenReturnUserPubId() {
        //given
        UUID expectedUserPubId = UUID.randomUUID();
        String token = jwtService.generateToken(expectedUserPubId, Role.ADMIN);

        //when
        UUID publicUserId = jwtService.extractUserPublicId(token);

        //then
        assertEquals(expectedUserPubId, publicUserId);
    }

    @Test
    void givenExpiredToken_whenExtractingUserPublicId_thenReturnUserPubId() {
        //given
        UUID expectedUserPubId = UUID.randomUUID();
        String token = jwtService.generateToken(expectedUserPubId, Role.ADMIN);
        waitForJwtToExpire();

        //when
        UUID publicUserId = jwtService.extractUserPublicId(token);
        //then
        assertEquals(expectedUserPubId, publicUserId);
    }

    @Test
    void givenValidToken_whenExtractingUserRole_thenReturnUserRole() {
        //given
        Role expectedUserRole = Role.ADMIN;
        String token = jwtService.generateToken(UUID.randomUUID(), expectedUserRole);

        //when
        String userRole = jwtService.extractUserRole(token);

        //then
        assertEquals(expectedUserRole.toString(), userRole);
    }

    @Test
    void givenExpiredToken_whenExtractingUserRole_thenReturnUserRole() {
        //given
        Role expectedUserRole = Role.ADMIN;
        String token = jwtService.generateToken(UUID.randomUUID(), expectedUserRole);
        waitForJwtToExpire();

        //when
        String userRole = jwtService.extractUserRole(token);

        //then
        assertEquals(expectedUserRole.toString(), userRole);
    }

    void waitForJwtToExpire() {
        try {
            Thread.sleep(jwtTimeToLive);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}