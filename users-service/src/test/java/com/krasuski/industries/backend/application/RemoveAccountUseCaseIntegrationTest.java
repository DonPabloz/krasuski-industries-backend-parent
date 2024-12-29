package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.exception.UserException;
import com.krasuski.industries.backend.domain.value.UserPubId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RemoveAccountUseCaseIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private RemoveAccountUseCase removeAccountUseCase;

    @Test
    void removeAccount_userExists_accountMarkedAsRemoved() {
        // given
        User user = createUser();

        // when
        removeAccountUseCase.removeAccount(user.getPubId());

        // then
        assertThat(userRepository.findByPubId(user.getPubId()).get().isMarkedAsRemoved()).isTrue();
    }

    @Test
    void removeAccount_userDoesNotExist_shouldThrowException() {
        // given
        UserPubId notExistingPubId = new UserPubId("notExistingPubId");

        // when && then
        assertThrows(UserException.class, () -> removeAccountUseCase.removeAccount(notExistingPubId));
    }
}