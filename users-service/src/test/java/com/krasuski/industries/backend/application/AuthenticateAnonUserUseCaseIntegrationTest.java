package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.AuthenticateAnonUserCommand;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.value.SessionTokenValue;
import com.krasuski.industries.backend.domain.value.UserPubId;
import com.krasuski.industries.backend.factory.UserTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthenticateAnonUserUseCaseIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private AuthenticateAnonUserUseCase authenticateAnonUserUseCase;

    @Test
    void authenticateAnonUser_userExistsAndValidSessionIsPassed_shouldAuthenticate() {
        // given
        User user = UserTestDataFactory.anAnonymousUser();
        userRepository.save(user);
        AuthenticateAnonUserCommand command = new AuthenticateAnonUserCommand(user.getPubId(), user.getAnonymousUserSession());

        // when && then
        assertDoesNotThrow(() -> authenticateAnonUserUseCase.authenticate(command));
    }

    @Test
    void authenticateAnonUser_userExistsAndInvalidSessionIsPassed_shouldNotAuthenticate() {
        // given
        User user = UserTestDataFactory.anAnonymousUser();
        userRepository.save(user);
        AuthenticateAnonUserCommand command = new AuthenticateAnonUserCommand(user.getPubId(), new SessionTokenValue("invalid-session"));

        // when && then
        assertThrows(Exception.class, () -> authenticateAnonUserUseCase.authenticate(command));
    }

    @Test
    void authenticateAnonUser_userDoesNotExist_shouldNotAuthenticate() {
        // given
        User user = UserTestDataFactory.anAnonymousUser();
        userRepository.save(user);
        AuthenticateAnonUserCommand command = new AuthenticateAnonUserCommand(new UserPubId("notExistingUser"), user.getAnonymousUserSession());

        // when && then
        assertThrows(Exception.class, () -> authenticateAnonUserUseCase.authenticate(command));
    }
}