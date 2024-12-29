package com.krasuski.industries.backend.service;

import com.krasuski.industries.backend.AbstractUnitTest;
import com.krasuski.industries.backend.ObjectMapper;
import com.krasuski.industries.backend.ObjectMapperImpl;
import com.krasuski.industries.backend.dto.user.request.ResetPasswordRequest;
import com.krasuski.industries.backend.dto.user.request.UpdatePrivacySettingsRequest;
import com.krasuski.industries.backend.dto.user.request.UpdateProfileRequest;
import com.krasuski.industries.backend.dto.user.response.PrivacySettingsResponse;
import com.krasuski.industries.backend.dto.user.response.ResetPasswordResponse;
import com.krasuski.industries.backend.dto.user.response.UserProfileInfoResponse;
import com.krasuski.industries.backend.entity.UserEntity;
import com.krasuski.industries.backend.exception.user.PasswordIncorrectException;
import com.krasuski.industries.backend.exception.user.UserNotFoundException;
import com.krasuski.industries.backend.repositories.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

class ProfileServiceTest extends AbstractUnitTest {

    private final ObjectMapper objectMapper = new ObjectMapperImpl();
    @Mock
    private UserRepository userRepository;
    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        profileService = new ProfileService(userRepository, objectMapper);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0, -1})
    void givenNotExistingUserId_whenGetUserProfileInfo_thenThrowUserNotFoundException(Long userId) {
        // given

        // when

        // then
        assertThrows(UserNotFoundException.class, () -> profileService.getUserProfileInfo(userId));
    }

    @Test
    void givenExistingUserId_whenGetUserProfileInfo_thenReturnUserProfileInfoResponse() {
        // given
        Long userId = 1L;

        // when
        when(userRepository.findById(userId)).thenReturn(Optional.of(getRegularUser()));
        UserProfileInfoResponse userProfileInfo = profileService.getUserProfileInfo(userId);

        // then
        assertNotNull(userProfileInfo);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(longs = {0, -1})
    void givenNotExistingUserId_whenUpdateProfile_thenThrowUserNotFoundException(Long userId) {
        // given

        // when

        // then
        assertThrows(UserNotFoundException.class, () -> profileService.updateProfile(userId, null));
    }

    @Test
    void givenExistingUserId_whenUpdateProfile_thenReturnUserProfileInfoResponse() {
        // given
        Long userId = 1L;
        String email = UUID.randomUUID() + "@gmail.com";
        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
        updateProfileRequest.setEmail(email);

        // when
        when(userRepository.findById(userId)).thenReturn(Optional.of(getRegularUser()));
        UserProfileInfoResponse userProfileInfo = profileService.updateProfile(userId, updateProfileRequest);

        // then
        assertNotNull(userProfileInfo);
        assertEquals(email, userProfileInfo.getEmail());
    }

    @Test
    void givenNotMatchingPasswords_whenUpdatePassword_thenThrowPasswordIncorrectException() {
        // given
        Long userId = 1L;
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setNewPassword("newPassword");
        resetPasswordRequest.setRepeatedNewPassword("repeatedNewPassword");

        // when

        // then
        assertThrows(PasswordIncorrectException.class, () -> profileService.updatePassword(userId, resetPasswordRequest));
    }

    @Test
    void givenNotExistingUserId_whenUpdatePassword_thenThrowUserNotFoundException() {
        // given
        Long userId = 1L;
        String newPassword = "newPassword";
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setNewPassword(newPassword);
        resetPasswordRequest.setRepeatedNewPassword(newPassword);

        // when

        // then
        assertThrows(UserNotFoundException.class, () -> profileService.updatePassword(userId, resetPasswordRequest));
    }

    @Test
    void givenOldPasswordIncorrect_whenUpdatePassword_thenThrowPasswordIncorrectException() {
        // given
        Long userId = 1L;
        String newPassword = "newPassword";
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setOldPassword(UUID.randomUUID().toString());
        resetPasswordRequest.setNewPassword(newPassword);
        resetPasswordRequest.setRepeatedNewPassword(newPassword);

        // when
        when(userRepository.findById(userId)).thenReturn(Optional.of(getRegularUser()));

        // then
        assertThrows(PasswordIncorrectException.class, () -> profileService.updatePassword(userId, resetPasswordRequest));
    }

    @Test
    void givenCorrectOldPassword_whenUpdatePassword_thenReturnResetPasswordResponse() {
        // given
        Long userId = 1L;
        String newPassword = "newPassword";
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setOldPassword(PASSWORD);
        resetPasswordRequest.setNewPassword(newPassword);
        resetPasswordRequest.setRepeatedNewPassword(newPassword);

        // when
        when(userRepository.findById(userId)).thenReturn(Optional.of(getRegularUser()));
        doReturn(getRegularUser()).when(userRepository).save(any());
        ResetPasswordResponse response = profileService.updatePassword(userId, resetPasswordRequest);

        // then
        assertNotNull(response);
        assertNotNull(response.getEmail());
    }

    @Test
    void givenNotExistingUserId_whenGetPrivacySettings_thenThrowUserNotFoundException() {
        // given
        Long userId = 1L;

        // when

        // then
        assertThrows(UserNotFoundException.class, () -> profileService.getPrivacySettings(userId));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(booleans = {true, false})
    void givenExistingUserId_whenGetPrivacySettings_thenReturnPrivacySettingsResponse(Boolean isNewsletterSubscriber) {
        // given
        Long userId = 1L;
        UserEntity regularUser = getRegularUser();
        regularUser.setIsNewsletterSubscriber(isNewsletterSubscriber);

        // when
        when(userRepository.findById(userId)).thenReturn(Optional.of(regularUser));
        PrivacySettingsResponse privacySettings = profileService.getPrivacySettings(userId);

        // then
        assertNotNull(privacySettings);
        assertEquals(isNewsletterSubscriber, privacySettings.getIsNewsletterSubscriber());
    }

    @Test
    void givenNotExistingUserId_whenUpdatePrivacySettings_thenThrowUserNotFoundException() {
        // given
        Long userId = 1L;

        // when

        // then
        assertThrows(UserNotFoundException.class, () -> profileService.updatePrivacySettings(userId, null));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void givenExistingUserId_whenUpdatePrivacySettings_thenReturnPrivacySettingsResponse(Boolean isNewsletterSubscriber) {
        // given
        Long userId = 1L;
        UserEntity regularUser = getRegularUser();
        regularUser.setIsNewsletterSubscriber(!isNewsletterSubscriber);

        UpdatePrivacySettingsRequest updatePrivacySettingsRequest = new UpdatePrivacySettingsRequest();
        updatePrivacySettingsRequest.setIsNewsletterSubscriber(isNewsletterSubscriber);

        // when
        when(userRepository.findById(userId)).thenReturn(Optional.of(regularUser));
        doReturn(regularUser).when(userRepository).save(any());
        PrivacySettingsResponse privacySettings = profileService.updatePrivacySettings(userId, updatePrivacySettingsRequest);

        // then
        assertNotNull(privacySettings);
        assertEquals(isNewsletterSubscriber, privacySettings.getIsNewsletterSubscriber());
    }
}