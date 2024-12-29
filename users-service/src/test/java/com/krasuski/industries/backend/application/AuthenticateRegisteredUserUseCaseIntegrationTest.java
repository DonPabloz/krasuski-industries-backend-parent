package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.BasicAuthenticationCommand;
import com.krasuski.industries.backend.application.command.TokenAuthCommand;
import com.krasuski.industries.backend.application.dto.RegisteredUserAuthenticationResponse;
import com.krasuski.industries.backend.application.port.PasswordEncoder;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.exception.UserException;
import com.krasuski.industries.backend.domain.value.AccessTokenValue;
import com.krasuski.industries.backend.domain.value.Password;
import com.krasuski.industries.backend.domain.value.RefreshTokenValue;
import com.krasuski.industries.backend.factory.UserTestDataFactory;
import com.krasuski.industries.backend.service.JwtService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthenticateRegisteredUserUseCaseIntegrationTest extends AbstractIntegrationTest {

    private static final Faker FAKER = new Faker();
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private static final JwtService JWT_SERVICE = new JwtService("secret", 100000);

    private AuthenticateRegisteredUserUseCase authenticateRegisteredUserUseCase;

    @BeforeEach
    void setUp() {
        authenticateRegisteredUserUseCase = new AuthenticateRegisteredUserUseCase(
                userRepository,
                PASSWORD_ENCODER,
                JWT_SERVICE
        );
    }

    @Test
    void basicAuth_userIsRegisteredAndVerifiedAndPasswordIsCorrect_shouldAuthenticate() {
        // given
        Password password = new Password(FAKER.internet().password());
        Password hashedPassword = PASSWORD_ENCODER.encode(password);
        User user = new UserTestDataFactory.UserBuilder()
                .withHashedPassword(hashedPassword)
                .withAccountVerified(true)
                .buildRegisteredUser();
        userRepository.save(user);
        BasicAuthenticationCommand basicAuthenticationCommand = new BasicAuthenticationCommand(
                user.getEmail(),
                password
        );

        // when
        RegisteredUserAuthenticationResponse registeredUserAuthenticationResponse = authenticateRegisteredUserUseCase.basicAuth(basicAuthenticationCommand);

        // then
        assertThat(registeredUserAuthenticationResponse).isNotNull();
        assertThat(registeredUserAuthenticationResponse.accessToken()).isNotNull();
        assertThat(registeredUserAuthenticationResponse.refreshToken()).isNotNull();
        assertThat(registeredUserAuthenticationResponse.userPubId()).isEqualTo(user.getPubId());
        assertThat(registeredUserAuthenticationResponse.userRole()).isEqualTo(user.getRole());

        UUID pubId = JWT_SERVICE.extractUserPublicId(registeredUserAuthenticationResponse.accessToken().value());
        String userRole = JWT_SERVICE.extractUserRole(registeredUserAuthenticationResponse.accessToken().value());
        assertThat(pubId).hasToString(user.getPubId().value());
        assertThat(userRole).isEqualTo(user.getRole().name());
    }

    @Test
    void basicAuth_userRegisteredAndVerifiedButPasswordIncorrect_throwAnException() {
        // given
        User user = new UserTestDataFactory.UserBuilder()
                .withHashedPassword(new Password(FAKER.internet().password()))
                .withAccountVerified(true)
                .buildRegisteredUser();
        userRepository.save(user);
        BasicAuthenticationCommand basicAuthenticationCommand = new BasicAuthenticationCommand(
                user.getEmail(),
                new Password(FAKER.internet().password())
        );

        // when && then
        assertThrows(UserException.class, () -> authenticateRegisteredUserUseCase.basicAuth(basicAuthenticationCommand));
    }

    @Test
    void basicAuth_userIsNotVerified_throwAnException() {
        // given
        Password password = new Password(FAKER.internet().password());
        Password hashedPassword = PASSWORD_ENCODER.encode(password);
        User user = new UserTestDataFactory.UserBuilder()
                .withHashedPassword(hashedPassword)
                .withAccountVerified(false)
                .buildRegisteredUser();
        userRepository.save(user);
        BasicAuthenticationCommand basicAuthenticationCommand = new BasicAuthenticationCommand(
                user.getEmail(),
                password
        );

        // when && then
        assertThrows(UserException.class, () -> authenticateRegisteredUserUseCase.basicAuth(basicAuthenticationCommand));
    }

    @Test
    void tokenBasedAuth_validJwt_shouldAuthenticate() {
        // given
        User user = new UserTestDataFactory.UserBuilder().buildRegisteredUser();
        userRepository.save(user);
        String accessTokenAsString = JWT_SERVICE.generateTokenDDD(user.getPubId().value(), user.getRole());
        TokenAuthCommand tokenAuthCommand = new TokenAuthCommand(
                new AccessTokenValue(accessTokenAsString),
                new RefreshTokenValue(FAKER.internet().uuid())
        );

        // when
        RegisteredUserAuthenticationResponse authResponse = authenticateRegisteredUserUseCase.tokenBasedAuth(tokenAuthCommand);

        // then
        assertThat(authResponse).isNotNull();
        assertThat(authResponse.accessToken()).isEqualTo(tokenAuthCommand.accessTokenValue());
        assertThat(authResponse.refreshToken()).isEqualTo(tokenAuthCommand.refreshTokenValue());
        assertThat(authResponse.userPubId()).isEqualTo(user.getPubId());
        assertThat(authResponse.userRole()).isEqualTo(user.getRole());
    }

    @Test
    void tokenBasedAuth_invalidJwtButValidRefreshToken_shouldCreateNewJwtAndAuthenticate() {
        // given
        User user = new UserTestDataFactory.UserBuilder().buildRegisteredUser();
        userRepository.save(user);
        JwtService jwtService = new JwtService("secret2", -100);
        authenticateRegisteredUserUseCase = new AuthenticateRegisteredUserUseCase(
                userRepository,
                PASSWORD_ENCODER,
                JWT_SERVICE
        );
        String accessTokenAsString = jwtService.generateTokenDDD(user.getPubId().value(), user.getRole());
        TokenAuthCommand tokenAuthCommand = new TokenAuthCommand(
                new AccessTokenValue(accessTokenAsString),
                new RefreshTokenValue(user.getRefreshTokens().get(0).getToken())
        );

        // when
        RegisteredUserAuthenticationResponse authResponse = authenticateRegisteredUserUseCase.tokenBasedAuth(tokenAuthCommand);

        // then
        assertThat(authResponse).isNotNull();
        assertThat(authResponse.accessToken()).isNotEqualTo(tokenAuthCommand.accessTokenValue());
        assertThat(authResponse.refreshToken()).isEqualTo(tokenAuthCommand.refreshTokenValue());
        assertThat(authResponse.userPubId()).isEqualTo(user.getPubId());
        assertThat(authResponse.userRole()).isEqualTo(user.getRole());
    }

    @Test
    void tokenBasedAuth_invalidJwtAndRefreshToken_shouldThrowException() {
        // given
        User user = new UserTestDataFactory.UserBuilder().buildRegisteredUser();
        userRepository.save(user);
        JwtService jwtService = new JwtService("secret2", -100);
        authenticateRegisteredUserUseCase = new AuthenticateRegisteredUserUseCase(
                userRepository,
                PASSWORD_ENCODER,
                JWT_SERVICE
        );
        String accessTokenAsString = jwtService.generateTokenDDD(user.getPubId().value(), user.getRole());
        TokenAuthCommand tokenAuthCommand = new TokenAuthCommand(
                new AccessTokenValue(accessTokenAsString),
                new RefreshTokenValue("invalidRefreshToken")
        );

        // when && then
        assertThrows(UserException.class, () -> authenticateRegisteredUserUseCase.tokenBasedAuth(tokenAuthCommand));
    }
}