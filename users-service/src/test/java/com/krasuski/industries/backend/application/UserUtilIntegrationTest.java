package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.exception.UserException;
import com.krasuski.industries.backend.domain.value.Email;
import com.krasuski.industries.backend.domain.value.UserPubId;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserUtilIntegrationTest extends AbstractIntegrationTest {

    private static final Faker FAKER = new Faker();

    @Test
    void findUserByPubId_userExists_shouldReturnUser() {
        //given
        User user = createUser();

        //when
        User userFound = UserUtil.findUserByPubId(userRepository, user.getPubId());

        //then
        assertThat(userFound.getPubId()).isEqualTo(user.getPubId());
    }

    @Test
    void findUserByPubId_userDoesNotExist_shouldThrowException() {
        //given
        createUser();
        UserPubId notExistingPubId = new UserPubId(FAKER.internet().uuid());

        //when && then
        assertThrows(UserException.class, () -> UserUtil.findUserByPubId(userRepository, notExistingPubId));
    }

    @Test
    void findUserByEmail_userExists_shouldReturnUser() {
        //given
        User user = createUser();

        //when
        User userFound = UserUtil.findUserByEmail(userRepository, user.getEmail());

        //then
        assertThat(userFound.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void findUserByEmail_userDoesNotExist_shouldThrowException() {
        //given
        createUser();
        Email notExistingEmail = new Email(FAKER.internet().emailAddress());

        //when && then
        assertThrows(UserException.class, () -> UserUtil.findUserByEmail(userRepository, notExistingEmail));
    }
}