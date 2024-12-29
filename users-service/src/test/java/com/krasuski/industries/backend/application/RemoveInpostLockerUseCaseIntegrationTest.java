package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.RemoveInpostLockerCommand;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.value.InpostExternalPubId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RemoveInpostLockerUseCaseIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private RemoveInpostLockerUseCase removeInpostLockerUseCase;

    @Test
    void removeInpostLocker_userAndLockerExists_lockerRemoved() {
        //given
        User user = createUser();
        InpostExternalPubId inpostExternalPubId = user.getInpostLockers().get(0);
        RemoveInpostLockerCommand removeInpostLockerCommand = new RemoveInpostLockerCommand(user.getPubId(), inpostExternalPubId);

        //when
        removeInpostLockerUseCase.removeInpostLocker(removeInpostLockerCommand);

        //then
        User savedUser = userRepository.findByPubId(user.getPubId()).get();
        Optional<InpostExternalPubId> removedLockerOptional = savedUser.getInpostLockers()
                .stream()
                .filter(inpostExternalPubId1 -> inpostExternalPubId1.equals(inpostExternalPubId))
                .findAny();
        assertThat(removedLockerOptional).isEmpty();
    }
}