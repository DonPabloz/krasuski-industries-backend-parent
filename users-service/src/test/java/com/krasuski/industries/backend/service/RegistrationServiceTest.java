package com.krasuski.industries.backend.service;

import com.krasuski.industries.backend.AbstractUnitTest;
import com.krasuski.industries.backend.ObjectMapper;
import com.krasuski.industries.backend.ObjectMapperImpl;
import com.krasuski.industries.backend.dto.user.request.RegisterUserRequest;
import com.krasuski.industries.backend.dto.user.request.ValidateEmailRequest;
import com.krasuski.industries.backend.entity.MultiFactorAuthenticationTokenEntity;
import com.krasuski.industries.backend.entity.UserEntity;
import com.krasuski.industries.backend.exception.user.VerificationTokenException;
import com.krasuski.industries.backend.messaging.MessageSender;
import com.krasuski.industries.backend.repositories.user.MultiFactorAuthenticationTokenRepository;
import com.krasuski.industries.backend.repositories.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class RegistrationServiceTest extends AbstractUnitTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private MultiFactorAuthenticationTokenRepository multiFactorAuthenticationTokenRepository;
    @Mock
    private MessageSender messageSender;
    private ObjectMapper objectMapper = new ObjectMapperImpl();

    private RegistrationService registrationService;

    @BeforeEach
    void setUp() {
        registrationService = new RegistrationService(userRepository, multiFactorAuthenticationTokenRepository, messageSender, objectMapper);
    }

    @Test
    void givenNotExistingMfaToken_whenValidateEmail_thenThrowVerificationTokenException() {
        // given
        ValidateEmailRequest validateEmailRequest = new ValidateEmailRequest();
        validateEmailRequest.setId(1L);
        validateEmailRequest.setValidationToken("token");

        // when
        when(multiFactorAuthenticationTokenRepository.findById(validateEmailRequest.getId())).thenReturn(java.util.Optional.empty());

        // then
        assertThrows(VerificationTokenException.class, () -> registrationService.validateEmail(validateEmailRequest));
    }

    @Test
    void givenExpiredMfaToken_whenValidateEmail_thenReturnGone() {
        // given
        ValidateEmailRequest validateEmailRequest = new ValidateEmailRequest();
        validateEmailRequest.setId(1L);
        validateEmailRequest.setValidationToken("token");

        MultiFactorAuthenticationTokenEntity multiFactorAuthenticationTokenEntity = new MultiFactorAuthenticationTokenEntity();
        multiFactorAuthenticationTokenEntity.setExpiryDate(new Date(System.currentTimeMillis() - 1000));
        // when
        when(multiFactorAuthenticationTokenRepository.findById(validateEmailRequest.getId())).thenReturn(java.util.Optional.of(multiFactorAuthenticationTokenEntity));

        // then
        assertEquals(HttpStatus.GONE, registrationService.validateEmail(validateEmailRequest));
    }

    @Test
    void givenInvalidToken_whenValidateEmail_thenReturnUnauthorized() {
        // given
        ValidateEmailRequest validateEmailRequest = new ValidateEmailRequest();
        validateEmailRequest.setId(1L);
        validateEmailRequest.setValidationToken("token");

        MultiFactorAuthenticationTokenEntity multiFactorAuthenticationTokenEntity = new MultiFactorAuthenticationTokenEntity();
        multiFactorAuthenticationTokenEntity.setExpiryDate(new Date(System.currentTimeMillis() + 1000));
        multiFactorAuthenticationTokenEntity.setToken("differentToken");

        // when
        when(multiFactorAuthenticationTokenRepository.findById(validateEmailRequest.getId())).thenReturn(java.util.Optional.of(multiFactorAuthenticationTokenEntity));

        // then
        assertEquals(HttpStatus.UNAUTHORIZED, registrationService.validateEmail(validateEmailRequest));
    }

    @Test
    void givenAlreadyEnabledUser_whenValidateEmail_thenReturnAlreadyReported() {
        // given
        String token = "token";
        ValidateEmailRequest validateEmailRequest = new ValidateEmailRequest();
        validateEmailRequest.setId(1L);
        validateEmailRequest.setValidationToken(token);

        MultiFactorAuthenticationTokenEntity multiFactorAuthenticationTokenEntity = new MultiFactorAuthenticationTokenEntity();
        multiFactorAuthenticationTokenEntity.setExpiryDate(new Date(System.currentTimeMillis() + 1000));
        multiFactorAuthenticationTokenEntity.setToken(token);
        UserEntity userEntity = new UserEntity();
        userEntity.setEnabled(true);
        multiFactorAuthenticationTokenEntity.setUserEntity(userEntity);

        // when
        when(multiFactorAuthenticationTokenRepository.findById(validateEmailRequest.getId())).thenReturn(java.util.Optional.of(multiFactorAuthenticationTokenEntity));

        // then
        assertEquals(HttpStatus.ALREADY_REPORTED, registrationService.validateEmail(validateEmailRequest));
    }

    @Test
    void givenValidRequest_whenValidateEmail_thenReturnAlreadyReported() {
        // given
        String token = "token";
        ValidateEmailRequest validateEmailRequest = new ValidateEmailRequest();
        validateEmailRequest.setId(1L);
        validateEmailRequest.setValidationToken(token);

        MultiFactorAuthenticationTokenEntity multiFactorAuthenticationTokenEntity = new MultiFactorAuthenticationTokenEntity();
        multiFactorAuthenticationTokenEntity.setExpiryDate(new Date(System.currentTimeMillis() + 1000));
        multiFactorAuthenticationTokenEntity.setToken(token);
        UserEntity userEntity = new UserEntity();
        userEntity.setEnabled(false);
        multiFactorAuthenticationTokenEntity.setUserEntity(userEntity);

        // when
        when(multiFactorAuthenticationTokenRepository.findById(validateEmailRequest.getId())).thenReturn(java.util.Optional.of(multiFactorAuthenticationTokenEntity));
        when(multiFactorAuthenticationTokenRepository.save(Mockito.any(MultiFactorAuthenticationTokenEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        // then
        assertEquals(HttpStatus.OK, registrationService.validateEmail(validateEmailRequest));
    }

    @Test
    void givenEmptyPassword_whenRegisterUser_thenThrowIllegalArgumentException() {
        // given
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setPassword("");

        // then
        assertThrows(ResponseStatusException.class, () -> registrationService.registerUser(registerUserRequest));
    }
}