package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.AddInpostLockerCommand;
import com.krasuski.industries.backend.application.port.UserRepository;
import com.krasuski.industries.backend.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AddInpostLockerUseCase {

    private final UserRepository userRepository;

    public AddInpostLockerUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addInpostLocker(AddInpostLockerCommand command) {
        User user = UserUtil.findUserByPubId(userRepository, command.userPubId());
        user.addInpostLocker(command.inpostExternalPubId());
        userRepository.save(user);
        log.info("Added Inpost locker {} to user {}", command.inpostExternalPubId(), command.userPubId());
    }
}
