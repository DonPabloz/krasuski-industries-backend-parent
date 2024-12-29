package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.AuthenticateAnonUserCommand;
import com.krasuski.industries.backend.application.port.UserRepository;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticateAnonUserUseCase {

    private final UserRepository userRepository;

    public AuthenticateAnonUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void authenticate(AuthenticateAnonUserCommand command) {
        User user = UserUtil.findUserByPubId(userRepository, command.userPubId());

        if(command.sessionToken().equals(user.getAnonymousUserSession())) {
            log.info("Anon user with pubId '{}' is authenticated.", command.userPubId().value());
        } else {
            log.info("Anon user with pubId '{}' is not authenticated.", command.userPubId().value());
            throw new UserException("User not authenticated.");
        }
    }
}
