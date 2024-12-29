package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.GeneratePasswordResetTokenCommand;
import com.krasuski.industries.backend.application.command.ResetPasswordByTokenCommand;
import com.krasuski.industries.backend.application.command.ResetPasswordCommand;
import com.krasuski.industries.backend.application.port.PasswordEncoder;
import com.krasuski.industries.backend.application.port.PasswordResetTokenRepository;
import com.krasuski.industries.backend.application.port.UserRepository;
import com.krasuski.industries.backend.domain.PasswordResetToken;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResetPasswordUseCase {

    private static final long EXPIRATION_TIME_IN_MINUTES = 60;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public ResetPasswordUseCase(UserRepository userRepository, PasswordEncoder passwordEncoder, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    public void resetPassword(ResetPasswordCommand command) {
        User user = UserUtil.findUserByPubId(userRepository, command.userPubId());
        if (!passwordEncoder.matches(command.oldPassword(), user.getHashedPassword())) {
            log.error("Old password is incorrect");
            throw new UserException("Old password is incorrect");
        }

        user.setHashedPassword(passwordEncoder.encode(command.newPassword()));
        userRepository.save(user);
        log.info("Password reset for user with pubId: {}", user.getPubId());
    }

    public void resetPasswordViaToken(ResetPasswordByTokenCommand command) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(command.passwordResetTokenValue())
                .orElseThrow(() -> {
                    log.error("Invalid password reset token");
                    return new UserException("Invalid password reset token");
                });
        if (!passwordResetToken.isTokenValid(command.passwordResetTokenValue())) {
            log.error("Invalid password reset token");
            throw new UserException("Invalid password reset token");
        }

        User user = passwordResetToken.getUser();
        user.setHashedPassword(passwordEncoder.encode(command.newPassword()));
        userRepository.save(user);
        log.info("Password reset via token for user with pubId: {}", user.getPubId());
    }

    public PasswordResetToken generatePasswordResetLink(GeneratePasswordResetTokenCommand command) {
        User user = UserUtil.findUserByEmail(userRepository, command.email());
        PasswordResetToken passwordResetToken = PasswordResetToken.create(user, EXPIRATION_TIME_IN_MINUTES);
        passwordResetTokenRepository.save(passwordResetToken);

        log.info("Password reset link generated for user with pubId: {}", user.getPubId());
        return passwordResetToken;
    }
}
