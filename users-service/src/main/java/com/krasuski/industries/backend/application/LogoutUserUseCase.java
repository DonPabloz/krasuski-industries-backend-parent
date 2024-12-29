package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.LogoutUserCommand;
import com.krasuski.industries.backend.application.port.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogoutUserUseCase {

    private final UserRepository userRepository;

    public LogoutUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void logoutUser(LogoutUserCommand command) {
        userRepository.findByPubId(command.userPubId())
                .ifPresent(user -> {
                    user.logout(command.refreshTokenValue());
                    userRepository.save(user);
                    log.info("User with pubId '{}' has been logged out.", command.userPubId().value());
                });
    }
}
