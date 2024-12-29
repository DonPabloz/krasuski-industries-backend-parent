package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.RegisterUserCommand;
import com.krasuski.industries.backend.application.port.PasswordEncoder;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.UserRole;
import com.krasuski.industries.backend.domain.value.Email;
import com.krasuski.industries.backend.domain.value.Password;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RegisterUserUseCaseIntegrationTest extends AbstractIntegrationTest{

    private static final Faker FAKER = new Faker();

    @Autowired
    private RegisterUserUseCase registerUserUseCase;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registerUser_properDataPassed_userAccountCreated() {
        //given
        RegisterUserCommand registerUserCommand = new RegisterUserCommand(
                new Email(FAKER.internet().emailAddress()),
                new Password(FAKER.internet().password()));

        //when
        registerUserUseCase.registerUser(registerUserCommand);

        //then
        User user = userRepository.findByEmail(registerUserCommand.email()).orElseThrow();
        assertThat(user.getEmail()).isEqualTo(registerUserCommand.email());
        assertThat(user.getRole()).isEqualTo(UserRole.REGISTERED_USER);
        assertTrue(passwordEncoder.matches(registerUserCommand.password(), user.getHashedPassword()));
        assertNull(user.getAnonymousUserSession().value());
        assertNotNull(user.getPubId());
        assertThat(user.isAccountVerified()).isFalse();
        assertThat(user.getCompanyAddresses()).isEmpty();
        assertThat(user.getPrivateAddresses()).isEmpty();
        assertThat(user.getInpostLockers()).isEmpty();
        assertThat(user.getRefreshTokens()).isEmpty();
    }

    @Test
    void registerUser_userAlreadyExists_userAccountNotCreated() {
        //given
        User user = createUser();
        RegisterUserCommand registerUserCommand = new RegisterUserCommand(user.getEmail(), new Password(FAKER.internet().password()));

        //when && then
        assertThrows(Exception.class, () -> registerUserUseCase.registerUser(registerUserCommand));
    }
}