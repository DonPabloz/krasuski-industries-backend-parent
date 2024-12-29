package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.exception.UserException;
import com.krasuski.industries.backend.domain.value.UserPubId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GetUserUseCaseIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private GetUserUseCase getUserUseCase;

    @Test
    void getUser_userExists_userReturned() {
        // given
        User user = createUser();

        // when
        User userFromGetUseCase = getUserUseCase.getUser(user.getPubId());

        // then
        assertThat(user.getPubId()).isEqualTo(userFromGetUseCase.getPubId());
    }

    @Test
    void getUser_userDoesNotExist_shouldThrowException() {
        // given
        User user = createUser();

        // when && then
        assertThrows(UserException.class, () -> getUserUseCase.getUser(new UserPubId("newPubId")));
    }
}