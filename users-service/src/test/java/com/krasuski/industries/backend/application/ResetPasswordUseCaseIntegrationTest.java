package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.GeneratePasswordResetTokenCommand;
import com.krasuski.industries.backend.application.command.ResetPasswordByTokenCommand;
import com.krasuski.industries.backend.application.command.ResetPasswordCommand;
import com.krasuski.industries.backend.application.port.PasswordEncoder;
import com.krasuski.industries.backend.application.port.PasswordResetTokenRepository;
import com.krasuski.industries.backend.domain.PasswordResetToken;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.exception.UserException;
import com.krasuski.industries.backend.domain.value.Email;
import com.krasuski.industries.backend.domain.value.Password;
import com.krasuski.industries.backend.domain.value.PasswordResetTokenValue;
import com.krasuski.industries.backend.factory.UserTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class ResetPasswordUseCaseIntegrationTest extends AbstractIntegrationTest {

    private static final long EXPIRATION_TIME_IN_MINUTES = 60;

    @Autowired
    private ResetPasswordUseCase resetPasswordUseCase;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Test
    void resetPassword_validOldPassword_shouldChangePassword() {
        // given
        Password oldPassword = new Password("oldPassword");
        Password newPassword = new Password("newPassword");
        Password hashedOldPassword = passwordEncoder.encode(oldPassword);
        User user = new UserTestDataFactory.UserBuilder().withHashedPassword(hashedOldPassword).buildRegisteredUser();
        userRepository.save(user);
        ResetPasswordCommand resetPasswordCommand = new ResetPasswordCommand(
                user.getPubId(),
                oldPassword,
                newPassword,
                newPassword
        );

        // when
        resetPasswordUseCase.resetPassword(resetPasswordCommand);

        // then
        User savedUser = userRepository.findByPubId(user.getPubId()).get();
        assertFalse(passwordEncoder.matches(oldPassword, savedUser.getHashedPassword()));
        assertTrue(passwordEncoder.matches(newPassword, savedUser.getHashedPassword()));
    }

    @Test
    void resetPassword_invalidOldPassword_shouldThrowException() {
        // given
        Password oldPassword = new Password("oldPassword");
        Password newPassword = new Password("newPassword");
        Password hashedOldPassword = passwordEncoder.encode(oldPassword);
        User user = new UserTestDataFactory.UserBuilder().withHashedPassword(hashedOldPassword).buildRegisteredUser();
        userRepository.save(user);
        ResetPasswordCommand resetPasswordCommand = new ResetPasswordCommand(
                user.getPubId(),
                new Password("invalidOldPassword"),
                newPassword,
                newPassword
        );

        // when && then
        assertThrows(UserException.class, () -> resetPasswordUseCase.resetPassword(resetPasswordCommand));
        User savedUser = userRepository.findByPubId(user.getPubId()).get();
        assertTrue(passwordEncoder.matches(oldPassword, savedUser.getHashedPassword()));
        assertFalse(passwordEncoder.matches(newPassword, savedUser.getHashedPassword()));
    }

    @Test
    void resetPassword_userDoesNotExist_shouldThrowException() {
        // given
        ResetPasswordCommand resetPasswordCommand = new ResetPasswordCommand(
                UserTestDataFactory.aRegisteredUser().getPubId(),
                new Password("oldPassword"),
                new Password("newPassword"),
                new Password("newPassword")
        );

        // when && then
        assertThrows(UserException.class, () -> resetPasswordUseCase.resetPassword(resetPasswordCommand));
    }

    @Test
    void resetPasswordViaToken_properTokenPassed_shouldResetPassword() {
        // given
        User user = createUser();
        PasswordResetToken passwordResetToken = PasswordResetToken.create(user, EXPIRATION_TIME_IN_MINUTES);
        passwordResetTokenRepository.save(passwordResetToken);
        Password newPassword = new Password("newPassword");
        ResetPasswordByTokenCommand resetPasswordByTokenCommand = new ResetPasswordByTokenCommand(
                passwordResetToken.getToken(),
                newPassword
        );

        // when
        resetPasswordUseCase.resetPasswordViaToken(resetPasswordByTokenCommand);

        // then
        User savedUser = userRepository.findByPubId(user.getPubId()).get();
        assertTrue(passwordEncoder.matches(newPassword, savedUser.getHashedPassword()));
    }

    @Test
    void resetPasswordViaToken_invalidTokenPassed_shouldThrowException() {
        // given
        User user = createUser();
        PasswordResetToken passwordResetToken = PasswordResetToken.create(user, EXPIRATION_TIME_IN_MINUTES);
        passwordResetTokenRepository.save(passwordResetToken);
        Password newPassword = new Password("newPassword");
        ResetPasswordByTokenCommand resetPasswordByTokenCommand = new ResetPasswordByTokenCommand(
                new PasswordResetTokenValue("invalidToken"),
                newPassword
        );

        // when && then
        assertThrows(UserException.class, () -> resetPasswordUseCase.resetPasswordViaToken(resetPasswordByTokenCommand));
    }

    @Test
    void resetPasswordViaToken_tokenExpired_shouldThrowException() {
        // given
        User user = createUser();
        PasswordResetToken passwordResetToken = PasswordResetToken.create(user, 0);
        passwordResetTokenRepository.save(passwordResetToken);
        Password newPassword = new Password("newPassword");
        ResetPasswordByTokenCommand resetPasswordByTokenCommand = new ResetPasswordByTokenCommand(
                passwordResetToken.getToken(),
                newPassword
        );

        // when && then
        assertThrows(UserException.class, () -> resetPasswordUseCase.resetPasswordViaToken(resetPasswordByTokenCommand));
    }

    @Test
    void generatePasswordResetLink_userExists_shouldGenerateToken() {
        // given
        User user = createUser();
        GeneratePasswordResetTokenCommand command = new GeneratePasswordResetTokenCommand(user.getEmail());

        //when
        PasswordResetToken passwordResetToken = resetPasswordUseCase.generatePasswordResetLink(command);

        //then
        assertNotNull(passwordResetToken);
        assertEquals(user.getPubId(), passwordResetToken.getUser().getPubId());
    }

    @Test
    void generatePasswordResetLink_userDoesNotExist_shouldThrowException() {
        // given
        GeneratePasswordResetTokenCommand command = new GeneratePasswordResetTokenCommand(new Email("not_exist@gmail.com"));

        // when && then
        assertThrows(UserException.class, () -> resetPasswordUseCase.generatePasswordResetLink(command));
    }
}