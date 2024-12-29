package com.krasuski.industries.backend.service;

import com.krasuski.industries.backend.ObjectMapper;
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
import com.krasuski.industries.backend.util.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProfileService {

    private static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = PasswordEncoder.getInstance();

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public ProfileService(UserRepository userRepository, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    public UserProfileInfoResponse getUserProfileInfo(Long userId) {
        UserEntity userEntity = getUserByIdOrThrowUserNotFoundException(userId);

        return objectMapper.userDetailsEntityToUserProfileInfoResponse(userEntity);
    }

    public UserProfileInfoResponse updateProfile(Long userId, UpdateProfileRequest updateProfileRequest) {
        log.info("Updating user profile with id: " + userId);
        UserEntity userEntity = userRepository.findById(userId)
                .map(userDetailsEntity1 -> updateUserWithValuesFromRequest(updateProfileRequest, userDetailsEntity1))
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist."));

        userRepository.save(userEntity);
        return objectMapper.userDetailsEntityToUserProfileInfoResponse(userEntity);
    }

    public ResetPasswordResponse updatePassword(Long userId, ResetPasswordRequest resetPasswordRequest) {
        log.info("Request for password reset by user: " + userId);

        if (!resetPasswordRequest.getNewPassword().equals(resetPasswordRequest.getRepeatedNewPassword())) {
            throw new PasswordIncorrectException("New password and repeated password don't match.");
        }

        return userRepository.findById(userId)
                .filter(userDetailsEntity -> {
                    if (B_CRYPT_PASSWORD_ENCODER.matches(resetPasswordRequest.getOldPassword(), userDetailsEntity.getPassword())) {
                        log.info("Old password matched - updating password.");
                        return true;
                    }
                    log.info("Existing password is incorrect.");
                    throw new PasswordIncorrectException("Existing password is incorrect.");
                })
                .map(userDetailsEntity -> {
                    userDetailsEntity.setPassword(B_CRYPT_PASSWORD_ENCODER.encode(resetPasswordRequest.getNewPassword()));
                    ResetPasswordResponse resp = new ResetPasswordResponse();
                    resp.setEmail(userRepository.save(userDetailsEntity).getEmail());

                    return resp;
                })
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist."));
    }

    public PrivacySettingsResponse getPrivacySettings(Long userId) {
        UserEntity user = getUserByIdOrThrowUserNotFoundException(userId);

        PrivacySettingsResponse privacySettingsResponse = new PrivacySettingsResponse();
        privacySettingsResponse.setIsNewsletterSubscriber(user.getIsNewsletterSubscriber());

        return privacySettingsResponse;
    }

    public PrivacySettingsResponse updatePrivacySettings(Long userId, UpdatePrivacySettingsRequest updatePrivacySettingsRequest) {
        UserEntity user = getUserByIdOrThrowUserNotFoundException(userId);
        user.setIsNewsletterSubscriber(updatePrivacySettingsRequest.getIsNewsletterSubscriber());
        userRepository.save(user);

        PrivacySettingsResponse privacySettingsResponse = new PrivacySettingsResponse();
        privacySettingsResponse.setIsNewsletterSubscriber(user.getIsNewsletterSubscriber());

        return privacySettingsResponse;
    }

    private UserEntity getUserByIdOrThrowUserNotFoundException(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User doesn't exist."));
    }

    private static UserEntity updateUserWithValuesFromRequest(UpdateProfileRequest updateProfileRequest, UserEntity userEntity) {
        if (StringUtils.isNotBlank(updateProfileRequest.getName())) {
            userEntity.setName(updateProfileRequest.getName());
        }
        if (StringUtils.isNotBlank(updateProfileRequest.getSurname())) {
            userEntity.setSurname(updateProfileRequest.getSurname());
        }
        if (StringUtils.isNotBlank(updateProfileRequest.getEmail())) {
            userEntity.setEmail(updateProfileRequest.getEmail());
        }
        if (StringUtils.isNotBlank(updateProfileRequest.getPhoneNumber())) {
            userEntity.setPhoneNumber(updateProfileRequest.getPhoneNumber());
        }
        if (StringUtils.isNotBlank(updateProfileRequest.getCompanyName())) {
            userEntity.setCompanyName(updateProfileRequest.getCompanyName());
        }
        if (StringUtils.isNotBlank(updateProfileRequest.getCompanyNip())) {
            userEntity.setCompanyNip(updateProfileRequest.getCompanyNip());
        }

        return userEntity;
    }
}
