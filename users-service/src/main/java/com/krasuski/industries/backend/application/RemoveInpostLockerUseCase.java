package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.RemoveInpostLockerCommand;
import com.krasuski.industries.backend.application.port.UserRepository;
import com.krasuski.industries.backend.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RemoveInpostLockerUseCase {

    private final UserRepository userRepository;

    public RemoveInpostLockerUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void removeInpostLocker(RemoveInpostLockerCommand command) {
        User user = UserUtil.findUserByPubId(userRepository, command.userPubId());
        user.removeInpostLocker(command.inpostExternalPubId());
        userRepository.save(user);
        log.info("Removed Inpost locker {} from user {}", command.inpostExternalPubId(), command.userPubId());
    }
}
