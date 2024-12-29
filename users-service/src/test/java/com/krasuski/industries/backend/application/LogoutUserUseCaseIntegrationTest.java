package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.LogoutUserCommand;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.value.RefreshTokenValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogoutUserUseCaseIntegrationTest extends AbstractIntegrationTest{

    private LogoutUserUseCase logoutUserUseCase;

    @BeforeEach
    void setUp() {
        logoutUserUseCase = new LogoutUserUseCase(userRepository);
    }

    @Test
    void logoutUser_userExistsAndHasRefreshToken_shouldInvalidateRefreshToken() {
        // given
        User user = createUser();
        var command = new LogoutUserCommand(user.getPubId(), new RefreshTokenValue(user.getRefreshTokens().get(0).getToken()));

        // when
        logoutUserUseCase.logoutUser(command);

        // then
        User savedUser = userRepository.findByPubId(user.getPubId()).get();
        assertFalse(savedUser.getRefreshTokens().get(0).isValid());
    }

    @Test
    void logoutUser_userExistsButHasDifferentRefreshToken_shouldNotInvalidateDifferentToken(){
        // given
        User user = createUser();
        var command = new LogoutUserCommand(user.getPubId(), new RefreshTokenValue("differentToken"));

        // when
        logoutUserUseCase.logoutUser(command);

        // then
        User savedUser = userRepository.findByPubId(user.getPubId()).get();
        assertTrue(savedUser.getRefreshTokens().get(0).isValid());
    }
}