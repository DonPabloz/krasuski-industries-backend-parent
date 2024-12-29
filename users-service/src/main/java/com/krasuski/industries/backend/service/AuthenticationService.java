package com.krasuski.industries.backend.service;

import com.krasuski.industries.backend.dto.user.request.AuthenticateJwtReq;
import com.krasuski.industries.backend.dto.user.request.AuthenticateUserRequest;
import com.krasuski.industries.backend.dto.user.response.AuthenticateJwtResponse;
import com.krasuski.industries.backend.dto.user.response.AuthenticateUserResponse;
import com.krasuski.industries.backend.entity.UserEntity;
import com.krasuski.industries.backend.exception.user.AccountNotActiveException;
import com.krasuski.industries.backend.exception.user.PasswordIncorrectException;
import com.krasuski.industries.backend.exception.user.UserNotFoundException;
import com.krasuski.industries.backend.repositories.user.UserRepository;
import com.krasuski.industries.backend.util.AuthCookieManager;
import com.krasuski.industries.backend.util.PasswordEncoder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;


@Service
@Slf4j
public class AuthenticationService {

    private static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = PasswordEncoder.getInstance();

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthCookieManager authCookieManager;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationService(UserRepository userRepository, JwtService jwtService, AuthCookieManager authCookieManager, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authCookieManager = authCookieManager;
        this.refreshTokenService = refreshTokenService;
    }

    public AuthenticateUserResponse authenticateUserByBasicAuth(AuthenticateUserRequest authenticateUserRequest, HttpServletResponse response) {
        UserEntity userEntity = findUserOrThrowUserNotFoundException(authenticateUserRequest);

        if (!isAccountActive(userEntity)) {
            throw new AccountNotActiveException("Account is not active.");
        }

        if (!isValidPassword(authenticateUserRequest, userEntity)) {
            throw new PasswordIncorrectException("Password incorrect.");
        }

        String refreshToken = refreshTokenService.createAndSaveRefreshToken(userEntity);
        String accessToken = jwtService.generateToken(userEntity.getPublicId(), userEntity.getUserRole().getRole());

        authCookieManager.addRefreshTokenCookieToResponse(response, refreshToken);
        authCookieManager.addAccessTokenCookieToResponse(response, accessToken);

        return new AuthenticateUserResponse(accessToken, userEntity.getName());
    }

    public AuthenticateJwtResponse authenticateJwt(AuthenticateJwtReq authenticateJwtReq) {
        String jwt = authenticateJwtReq.getJwt();
        String refreshToken = authenticateJwtReq.getRefreshToken();
        UUID userPublicId = jwtService.extractUserPublicId(jwt);
        UserEntity userEntity = getUserByPubIdOrThrowUserNotFoundException(userPublicId);

        if (!jwtService.isJwtValid(jwt) && !refreshTokenService.isRefreshTokenValid(refreshToken, userEntity.getId())) {
            log.info("User with publicId: {} not authorized. JWT and refresh token are not valid.", userEntity.getPublicId());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        if (!jwtService.isJwtValid(jwt)) {
            log.info("Regenerating jwt for user with publicId: {}.", userEntity.getPublicId());
            jwt = jwtService.generateToken(userEntity.getPublicId(), userEntity.getUserRole().getRole());
        }

        String role = jwtService.extractUserRole(jwt);

        AuthenticateJwtResponse authenticateJwtResponse = new AuthenticateJwtResponse();
        authenticateJwtResponse.setAccessToken(jwt);
        authenticateJwtResponse.setRefreshToken(refreshToken);
        authenticateJwtResponse.setUserId(userEntity.getId().toString());
        authenticateJwtResponse.setRole(role);

        return authenticateJwtResponse;
    }

    public void logoutUser(HttpServletResponse response) {
        authCookieManager.removeAccessTokenCookieFromResponse(response);
        authCookieManager.removeRefreshTokenCookieFromResponse(response);
    }

    private boolean isAccountActive(UserEntity userEntity) {
        return Boolean.TRUE.equals(userEntity.getEnabled());
    }

    private boolean isValidPassword(AuthenticateUserRequest authenticateUserRequest, UserEntity userEntity) {
        return B_CRYPT_PASSWORD_ENCODER.matches(authenticateUserRequest.getPassword(), userEntity.getPassword());
    }

    private UserEntity findUserOrThrowUserNotFoundException(AuthenticateUserRequest authenticateUserRequest) {
        return userRepository.findByEmail(authenticateUserRequest.getMail())
                .orElseThrow(() -> {
                    log.error("User with email: {} not found.", authenticateUserRequest.getMail());
                    throw new UserNotFoundException("User doesn't exist.");
                });
    }

    private UserEntity getUserByPubIdOrThrowUserNotFoundException(UUID userPublicId) {
        return userRepository.findByPublicId(userPublicId)
                .orElseThrow(() -> {
                    log.error("User with publicId: {} not found.", userPublicId);
                    throw new UserNotFoundException("User doesn't exist.");
                });
    }
}
