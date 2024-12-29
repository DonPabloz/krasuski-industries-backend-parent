package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.VerifyEmailCommand;
import com.krasuski.industries.backend.application.port.VerificationTokenRepository;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.VerificationToken;
import com.krasuski.industries.backend.domain.exception.VerificationTokenException;
import org.apache.commons.lang3.NotImplementedException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VerifyEmailUseCaseIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private VerifyEmailUseCase verifyEmailUseCase;

    @Test
    void verifyEmail_properVerificationTokenPassed_userAccountVerified() {
        //given
        User user = createUser();
        VerificationToken verificationToken = createVerificationToken(user, 60);
        VerifyEmailCommand verifyEmailCommand = new VerifyEmailCommand(verificationToken.getToken());

        //when
        verifyEmailUseCase.verifyEmail(verifyEmailCommand);

        //then
        User verifiedUser = userRepository.findByEmail(user.getEmail()).get();
        assertThat(verifiedUser.isAccountVerified()).isTrue();
    }

    @Test
    void verifyEmail_verificationTokenExpired_verificationTokenExceptionThrown() {
        //given
        User user = createUser();
        VerificationToken verificationToken = createVerificationToken(user, 0);
        VerifyEmailCommand verifyEmailCommand = new VerifyEmailCommand(verificationToken.getToken());

        //when && then
        assertThrows(VerificationTokenException.class, () -> verifyEmailUseCase.verifyEmail(verifyEmailCommand));
    }

    @Test
    void verifyEmail_verificationTokenNotFound_verificationTokenExceptionThrown() {
        //given
        User user = createUser();
        createVerificationToken(user, 60);
        VerifyEmailCommand verifyEmailCommand = new VerifyEmailCommand("some new token");

        //when && then
        assertThrows(VerificationTokenException.class, () -> verifyEmailUseCase.verifyEmail(verifyEmailCommand));
    }

    private VerificationToken createVerificationToken(User user, long expirationTimeInMinutes) {
        VerificationToken verificationToken = VerificationToken.createNew(user, expirationTimeInMinutes);
        verificationTokenRepository.save(verificationToken);

        return verificationToken;
    }
}