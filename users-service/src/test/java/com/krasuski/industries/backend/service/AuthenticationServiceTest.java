package com.krasuski.industries.backend.service;

import com.krasuski.industries.backend.AbstractUnitTest;
import com.krasuski.industries.backend.dto.user.request.AuthenticateJwtReq;
import com.krasuski.industries.backend.dto.user.request.AuthenticateUserRequest;
import com.krasuski.industries.backend.dto.user.response.AuthenticateJwtResponse;
import com.krasuski.industries.backend.dto.user.response.AuthenticateUserResponse;
import com.krasuski.industries.backend.entity.UserEntity;
import com.krasuski.industries.backend.exception.user.AccountNotActiveException;
import com.krasuski.industries.backend.exception.user.PasswordIncorrectException;
import com.krasuski.industries.backend.exception.user.UserNotFoundException;
import com.krasuski.industries.backend.repositories.user.UserRepository;
import com.krasuski.industries.backend.util.AuthCookieManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest extends AbstractUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private AuthCookieManager authCookieManager;
    @Mock
    private JwtService jwtService;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(userRepository, jwtService, authCookieManager, refreshTokenService);
    }

    @Test
    void givenUserNotFound_whenAuthenticateUser_thenThrowUserNotFoundException() {
        // given
        AuthenticateUserRequest authenticateUserRequest = getAuthenticateUserRequest();

        // when
        when(userRepository.findByEmail(authenticateUserRequest.getMail())).thenReturn(Optional.empty());

        // then
        assertThrows(UserNotFoundException.class, () -> authenticationService.authenticateUserByBasicAuth(authenticateUserRequest, null));
    }

    @Test
    void givenUserNotEnabled_whenAuthenticateUser_thenThrowAccountNotActiveException() {
        // given
        AuthenticateUserRequest authenticateUserRequest = getAuthenticateUserRequest();
        UserEntity user = new UserEntity();
        user.setEnabled(false);

        // when
        when(userRepository.findByEmail(authenticateUserRequest.getMail())).thenReturn(Optional.of(user));

        // then
        assertThrows(AccountNotActiveException.class, () -> authenticationService.authenticateUserByBasicAuth(authenticateUserRequest, null));
    }

    @Test
    void givenInvalidPassword_whenAuthenticateUser_thenThrowInvalidPasswordException() {
        // given
        AuthenticateUserRequest authenticateUserRequest = getAuthenticateUserRequest();
        UserEntity user = new UserEntity();
        user.setEnabled(true);
        user.setPassword(B_CRYPT_PASSWORD_ENCODER.encode("otherPassword"));

        // when
        when(userRepository.findByEmail(authenticateUserRequest.getMail())).thenReturn(Optional.of(user));

        // then
        assertThrows(PasswordIncorrectException.class, () -> authenticationService.authenticateUserByBasicAuth(authenticateUserRequest, null));
    }

    @Test
    void givenValidUser_whenAuthenticateUser_shouldGenerateRefreshTokenAndThenReturnAuthenticateUserResponse() {
        // given
        AuthenticateUserRequest authenticateUserRequest = getAuthenticateUserRequest();
        UserEntity user = getRegularUser();
        String jwt = "jwt";
        String refreshToken = "ref_token";

        // when
        when(userRepository.findByEmail(authenticateUserRequest.getMail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user.getPublicId(), user.getUserRole().getRole())).thenReturn(jwt);
        when(refreshTokenService.createAndSaveRefreshToken(user)).thenReturn(refreshToken);

        AuthenticateUserResponse authenticateUserResponse = authenticationService.authenticateUserByBasicAuth(authenticateUserRequest, null);

        // then
        assertEquals(jwt, authenticateUserResponse.getJwt());
    }

    @Test
    void givenJwtNotValidAndRefreshTokenNotValid_whenAuthenticateJwt_shouldThrowUnauthorizedException() {
        // given
        AuthenticateJwtReq authenticateJwtReq = new AuthenticateJwtReq();
        UserEntity user = getRegularUser();

        // when
        when(jwtService.extractUserPublicId(authenticateJwtReq.getJwt())).thenReturn(user.getPublicId());
        when(userRepository.findByPublicId(user.getPublicId())).thenReturn(Optional.of(user));
        when(jwtService.isJwtValid(authenticateJwtReq.getJwt())).thenReturn(false);
        when(refreshTokenService.isRefreshTokenValid(authenticateJwtReq.getRefreshToken(), user.getId())).thenReturn(false);

        // then
        assertThrows(ResponseStatusException.class, () -> authenticationService.authenticateJwt(authenticateJwtReq));
    }

    @Test
    void givenJwtNotValidAndRefreshTokenValid_whenAuthenticateJwt_shouldPass() {
        // given
        AuthenticateJwtReq authenticateJwtReq = new AuthenticateJwtReq();
        UserEntity user = getRegularUser();

        // when
        when(jwtService.extractUserPublicId(authenticateJwtReq.getJwt())).thenReturn(user.getPublicId());
        when(userRepository.findByPublicId(user.getPublicId())).thenReturn(Optional.of(user));
        when(jwtService.isJwtValid(authenticateJwtReq.getJwt())).thenReturn(false);
        when(refreshTokenService.isRefreshTokenValid(authenticateJwtReq.getRefreshToken(), user.getId())).thenReturn(true);
        AuthenticateJwtResponse authenticateJwtResponse = authenticationService.authenticateJwt(authenticateJwtReq);

        // then
        assertNotNull(authenticateJwtResponse);
    }

    @Test
    void givenJwtValid_whenAuthenticateJwt_shouldPass() {
        // given
        AuthenticateJwtReq authenticateJwtReq = new AuthenticateJwtReq();
        UserEntity user = getRegularUser();

        // when
        when(jwtService.extractUserPublicId(authenticateJwtReq.getJwt())).thenReturn(user.getPublicId());
        when(userRepository.findByPublicId(user.getPublicId())).thenReturn(Optional.of(user));
        when(jwtService.isJwtValid(authenticateJwtReq.getJwt())).thenReturn(true);
        AuthenticateJwtResponse authenticateJwtResponse = authenticationService.authenticateJwt(authenticateJwtReq);

        // then
        assertNotNull(authenticateJwtResponse);
    }

    private AuthenticateUserRequest getAuthenticateUserRequest() {
        AuthenticateUserRequest authenticateUserRequest = new AuthenticateUserRequest();
        authenticateUserRequest.setMail("mail");
        authenticateUserRequest.setPassword("password");

        return authenticateUserRequest;
    }
}