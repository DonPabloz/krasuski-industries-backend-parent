package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.BasicAuthenticationCommand;
import com.krasuski.industries.backend.application.command.TokenAuthCommand;
import com.krasuski.industries.backend.application.dto.RegisteredUserAuthenticationResponse;
import com.krasuski.industries.backend.application.port.PasswordEncoder;
import com.krasuski.industries.backend.application.port.UserRepository;
import com.krasuski.industries.backend.domain.RefreshToken;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.exception.UserException;
import com.krasuski.industries.backend.domain.UserRole;
import com.krasuski.industries.backend.domain.value.AccessTokenValue;
import com.krasuski.industries.backend.domain.value.RefreshTokenValue;
import com.krasuski.industries.backend.domain.value.UserPubId;
import com.krasuski.industries.backend.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthenticateRegisteredUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${refresh-token-time-to-live}")
    private int refreshTokenTimeToLive;

    public AuthenticateRegisteredUserUseCase(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public RegisteredUserAuthenticationResponse basicAuth(BasicAuthenticationCommand basicAuthenticationCommand) {
        User user = UserUtil.findUserByEmail(userRepository, basicAuthenticationCommand.mail());

        if (!passwordEncoder.matches(basicAuthenticationCommand.password(), user.getHashedPassword())) {
            log.error("Password for user with pubId '{}' incorrect.", user.getPubId());
            throw new UserException("Password incorrect.");
        }

        if (!user.isAccountVerified()) {
            log.error("Account of user with pubId '{}' is not verified.", user.getPubId());
            throw new UserException("Account is not verified.");
        }

        AccessTokenValue accessToken = generateNewAccessToken(user);
        RefreshToken refreshToken = user.createNewRefreshToken(refreshTokenTimeToLive);

        return new RegisteredUserAuthenticationResponse(accessToken, new RefreshTokenValue(refreshToken.getToken()), user.getPubId(), user.getRole());
    }

    public RegisteredUserAuthenticationResponse tokenBasedAuth(TokenAuthCommand tokenAuthCommand) {
        UserPubId userPubId = new UserPubId(jwtService.extractUserPublicId(tokenAuthCommand.accessTokenValue().value()).toString());
        String userRole = jwtService.extractUserRole(tokenAuthCommand.accessTokenValue().value());

        if (jwtService.isJwtValid(tokenAuthCommand.accessTokenValue().value())) {
            return new RegisteredUserAuthenticationResponse(
                    tokenAuthCommand.accessTokenValue(),
                    tokenAuthCommand.refreshTokenValue(),
                    userPubId,
                    UserRole.valueOf(userRole)
            );
        }

        User user = UserUtil.findUserByPubId(userRepository, userPubId);
        if (user.isRefreshTokenValid(tokenAuthCommand.refreshTokenValue().value())) {
            return new RegisteredUserAuthenticationResponse(
                    generateNewAccessToken(user),
                    tokenAuthCommand.refreshTokenValue(),
                    userPubId,
                    UserRole.valueOf(userRole)
            );
        }

        log.error("No valid access token or refresh token passed for user with pubId '{}'.", userPubId);
        throw new UserException("No valid access token or refresh token.");
    }

    private AccessTokenValue generateNewAccessToken(User user) {
        return new AccessTokenValue(jwtService.generateTokenDDD(user.getPubId().value(), user.getRole()));
    }
}
