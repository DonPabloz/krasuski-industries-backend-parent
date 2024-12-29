package com.krasuski.industries.backend.controller;

import com.krasuski.industries.backend.application.AuthenticateRegisteredUserUseCase;
import com.krasuski.industries.backend.application.LogoutUserUseCase;
import com.krasuski.industries.backend.application.command.BasicAuthenticationCommand;
import com.krasuski.industries.backend.application.command.LogoutUserCommand;
import com.krasuski.industries.backend.application.command.TokenAuthCommand;
import com.krasuski.industries.backend.application.dto.RegisteredUserAuthenticationResponse;
import com.krasuski.industries.backend.domain.value.*;
import com.krasuski.industries.backend.dto.user.request.AuthenticateJwtReq;
import com.krasuski.industries.backend.dto.user.request.AuthenticateUserRequest;
import com.krasuski.industries.backend.dto.user.request.LogoutUserRequest;
import com.krasuski.industries.backend.dto.user.response.AuthenticateJwtResponse;
import com.krasuski.industries.backend.dto.user.response.AuthenticateUserResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
@Validated
public class AuthController {

    private final AuthenticateRegisteredUserUseCase authenticateRegisteredUserUseCase;
    private final LogoutUserUseCase logoutUserUseCase;

    public AuthController(AuthenticateRegisteredUserUseCase authenticateRegisteredUserUseCase, LogoutUserUseCase logoutUserUseCase) {
        this.authenticateRegisteredUserUseCase = authenticateRegisteredUserUseCase;
        this.logoutUserUseCase = logoutUserUseCase;
    }

    //TODO validate if server sides cookies are set in API Gateway.
    @PostMapping(value = "/basic")
    public ResponseEntity<AuthenticateUserResponse> basicAuth(@RequestBody AuthenticateUserRequest authenticateUserRequest, HttpServletResponse response) {
        BasicAuthenticationCommand basicAuthenticationCommand = new BasicAuthenticationCommand(
                new Email(authenticateUserRequest.getMail()),
                new Password(authenticateUserRequest.getPassword())
        );
        RegisteredUserAuthenticationResponse registeredUserAuthenticationResponse = authenticateRegisteredUserUseCase.basicAuth(basicAuthenticationCommand);
        AuthenticateUserResponse authenticateUserResponse = new AuthenticateUserResponse(
                registeredUserAuthenticationResponse.accessToken().value(),
                registeredUserAuthenticationResponse.userPubId().value()
        );
        return ResponseEntity.status(HttpStatus.OK).body(authenticateUserResponse);
    }

    @PostMapping(value = "/jwt")
    public ResponseEntity<AuthenticateJwtResponse> jwtAuth(@Valid @RequestBody AuthenticateJwtReq authenticateJwtReq) {
        TokenAuthCommand tokenAuthCommand = new TokenAuthCommand(
                new AccessTokenValue(authenticateJwtReq.getJwt()),
                new RefreshTokenValue(authenticateJwtReq.getRefreshToken())
        );
        RegisteredUserAuthenticationResponse registeredUserAuthenticationResponse = authenticateRegisteredUserUseCase.tokenBasedAuth(tokenAuthCommand);
        AuthenticateJwtResponse authenticateJwtResponse = new AuthenticateJwtResponse();
        authenticateJwtResponse.setAccessToken(registeredUserAuthenticationResponse.accessToken().value());
        authenticateJwtResponse.setRefreshToken(registeredUserAuthenticationResponse.refreshToken().value());
        authenticateJwtResponse.setUserId(registeredUserAuthenticationResponse.userPubId().value());
        authenticateJwtResponse.setRole(registeredUserAuthenticationResponse.userRole().toString());

        return ResponseEntity.status(HttpStatus.OK).body(authenticateJwtResponse);
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<String> logoutUser(@RequestBody LogoutUserRequest logoutUserRequest, HttpServletResponse response) {
        LogoutUserCommand logoutUserCommand = new LogoutUserCommand(
                new UserPubId(logoutUserRequest.userPubId()),
                new RefreshTokenValue(logoutUserRequest.refreshToken())
        );
        logoutUserUseCase.logoutUser(logoutUserCommand);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
