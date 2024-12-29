package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.AddInpostLockerCommand;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.value.InpostExternalPubId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AddInpostLockerUseCaseIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    AddInpostLockerUseCase addInpostLockerUseCase;

    @Test
    void addInpostLocker_properDataPassed_shouldAddInpostLocker() {
        //given
        User user = createRegisteredUserWithoutAddresses();
        InpostExternalPubId inpostExternalPubId = new InpostExternalPubId(UUID.randomUUID().toString());
        AddInpostLockerCommand addInpostLockerCommand = new AddInpostLockerCommand(
                user.getPubId(),
                inpostExternalPubId
        );

        //when
        addInpostLockerUseCase.addInpostLocker(addInpostLockerCommand);

        //then
        User userWithInpostLocker = userRepository.findByPubId(user.getPubId()).orElseThrow();
        Optional<InpostExternalPubId> adddedInpostLocker = userWithInpostLocker.getInpostLockers()
                .stream()
                .filter(inpostLocker -> inpostLocker.equals(inpostExternalPubId))
                .findAny();
        assertThat(adddedInpostLocker).isPresent();
    }
}