package com.krasuski.industries.backend.controller;

import com.krasuski.industries.backend.application.GetUserUseCase;
import com.krasuski.industries.backend.application.RegisterUserUseCase;
import com.krasuski.industries.backend.application.ResetPasswordUseCase;
import com.krasuski.industries.backend.application.VerifyEmailUseCase;
import com.krasuski.industries.backend.application.command.RegisterUserCommand;
import com.krasuski.industries.backend.application.command.ResetPasswordCommand;
import com.krasuski.industries.backend.application.command.VerifyEmailCommand;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.value.Email;
import com.krasuski.industries.backend.domain.value.Password;
import com.krasuski.industries.backend.domain.value.UserPubId;
import com.krasuski.industries.backend.dto.user.request.*;
import com.krasuski.industries.backend.dto.user.response.PrivacySettingsResponse;
import com.krasuski.industries.backend.dto.user.response.ResetPasswordResponse;
import com.krasuski.industries.backend.dto.user.response.UserProfileInfoResponse;
import com.krasuski.industries.backend.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.krasuski.industries.backend.util.CustomHeaders.USER_ID_HEADER_NAME;

@RestController
@RequestMapping(value = "/user")
@Validated
public class UserController {

    private final ProfileService userProfileService;
    private final RegisterUserUseCase registerUserUseCase;
    private final VerifyEmailUseCase verifyEmailUseCase;
    private final GetUserUseCase getUserUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    public UserController(ProfileService userProfileService, RegisterUserUseCase registerUserUseCase, VerifyEmailUseCase verifyEmailUseCase, GetUserUseCase getUserUseCase, ResetPasswordUseCase resetPasswordUseCase) {
        this.userProfileService = userProfileService;
        this.registerUserUseCase = registerUserUseCase;
        this.verifyEmailUseCase = verifyEmailUseCase;
        this.getUserUseCase = getUserUseCase;
        this.resetPasswordUseCase = resetPasswordUseCase;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        RegisterUserCommand registerUserCommand = new RegisterUserCommand(
                new Email(registerUserRequest.getEmail()),
                new Password(registerUserRequest.getPassword())
        );
        registerUserUseCase.registerUser(registerUserCommand);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "/validate")
    public ResponseEntity<String> validateEmail(@RequestBody ValidateEmailRequest validateEmailRequest) {
        VerifyEmailCommand verifyEmailCommand = new VerifyEmailCommand(
                validateEmailRequest.getValidationToken()
        );
        verifyEmailUseCase.verifyEmail(verifyEmailCommand);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/profile")
    public ResponseEntity<UserProfileInfoResponse> userProfileInfo(@RequestHeader(USER_ID_HEADER_NAME) Long userId) {
        User user = getUserUseCase.getUser(new UserPubId(userId.toString()));
        UserProfileInfoResponse userProfileInfoResponse = new UserProfileInfoResponse();
        userProfileInfoResponse.setEmail(user.getEmail().value());

        return ResponseEntity.status(HttpStatus.OK).body(userProfileInfoResponse);
    }

    @PatchMapping(value = "/profile")
    @Deprecated
    public ResponseEntity<UserProfileInfoResponse> updateProfile(
            @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(userProfileService.updateProfile(userId, updateProfileRequest));
    }

    @PatchMapping(value = "/password")
    public ResponseEntity<ResetPasswordResponse> updatePassword(
            @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        ResetPasswordCommand resetPasswordCommand = new ResetPasswordCommand(
                new UserPubId(userId.toString()),
                new Password(resetPasswordRequest.getOldPassword()),
                new Password(resetPasswordRequest.getNewPassword()),
                new Password(resetPasswordRequest.getRepeatedNewPassword())
        );
        resetPasswordUseCase.resetPassword(resetPasswordCommand);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //TODO add
    @GetMapping(value = "/privacy")
    public ResponseEntity<PrivacySettingsResponse> getPrivacySettings(
            @RequestHeader(USER_ID_HEADER_NAME) Long userId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userProfileService.getPrivacySettings(userId));
    }

    //TODO add
    @PatchMapping(value = "/privacy")
    public ResponseEntity<PrivacySettingsResponse> updatePrivacySettings(
            @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @Valid @RequestBody UpdatePrivacySettingsRequest updatePrivacySettingsRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userProfileService.updatePrivacySettings(userId, updatePrivacySettingsRequest));
    }
}
