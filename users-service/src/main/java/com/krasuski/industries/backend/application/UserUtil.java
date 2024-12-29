package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.port.UserRepository;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.exception.UserException;
import com.krasuski.industries.backend.domain.value.Email;
import com.krasuski.industries.backend.domain.value.UserPubId;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class UserUtil {

    static User findUserByPubId(UserRepository userRepository, UserPubId userPubId) {
        return userRepository.findByPubId(userPubId)
                .orElseThrow(() -> {
                    log.error("User with pubId '{}' not found.", userPubId.value());
                    return new UserException("User not found.");
                });
    }

    static User findUserByEmail(UserRepository userRepository, Email email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User with email '{}' not found.", email.value());
                    return new UserException("User not found.");
                });
    }
}
