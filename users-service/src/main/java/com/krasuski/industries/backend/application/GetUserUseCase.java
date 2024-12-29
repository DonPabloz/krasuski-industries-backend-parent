package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.port.UserRepository;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.value.UserPubId;
import org.springframework.stereotype.Service;

@Service
public class GetUserUseCase {

    private final UserRepository userRepository;

    public GetUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(UserPubId pubId) {
        return UserUtil.findUserByPubId(userRepository, pubId);
    }
}
