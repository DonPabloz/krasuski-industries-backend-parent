package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.port.UserRepository;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.value.UserPubId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RemoveAccountUseCase {

    private final UserRepository userRepository;

    public RemoveAccountUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void removeAccount(UserPubId pubId) {
        User user = UserUtil.findUserByPubId(userRepository, pubId);
        user.setRemoved(true);
        userRepository.save(user);
        log.info("User with pubId: {} has been removed", pubId);
    }
}
