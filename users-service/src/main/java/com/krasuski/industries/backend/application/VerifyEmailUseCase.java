package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.VerifyEmailCommand;
import com.krasuski.industries.backend.application.port.UserRepository;
import com.krasuski.industries.backend.application.port.VerificationTokenRepository;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.VerificationToken;
import com.krasuski.industries.backend.domain.exception.VerificationTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VerifyEmailUseCase {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    public VerifyEmailUseCase(UserRepository userRepository, VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public void verifyEmail(VerifyEmailCommand verifyEmailCommand) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(verifyEmailCommand.token())
                .orElseThrow(() -> {
                    log.error("Token not found.");
                    throw new VerificationTokenException("Token not found");
                });

        if (verificationToken.isExpired()) {
            log.error("Token expired.");
            throw new VerificationTokenException("Token expired.");
        }

        User user = verificationToken.getUser();
        user.verifyAccount();
        userRepository.save(user);
        log.info("User account verified.");
    }
}
