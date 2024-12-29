package com.krasuski.industries.backend.service;

import com.krasuski.industries.backend.ObjectMapper;
import com.krasuski.industries.backend.dto.account.AccountCreationDto;
import com.krasuski.industries.backend.dto.user.request.RegisterUserRequest;
import com.krasuski.industries.backend.dto.user.request.ValidateEmailRequest;
import com.krasuski.industries.backend.entity.MultiFactorAuthenticationTokenEntity;
import com.krasuski.industries.backend.entity.UserEntity;
import com.krasuski.industries.backend.exception.user.VerificationTokenException;
import com.krasuski.industries.backend.messaging.MessageSender;
import com.krasuski.industries.backend.repositories.user.MultiFactorAuthenticationTokenRepository;
import com.krasuski.industries.backend.repositories.user.UserRepository;
import com.krasuski.industries.backend.util.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class RegistrationService {

    private static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = PasswordEncoder.getInstance();
    private static final String SLASH = "/";

    @Value("${registration-token-time-to-live}")
    private int registrationTokenTimeToLive;

    private final UserRepository userRepository;
    private final MultiFactorAuthenticationTokenRepository multiFactorAuthenticationTokenRepository;
    private final MessageSender messageSender;
    private final ObjectMapper objectMapper;

    public RegistrationService(UserRepository userRepository, MultiFactorAuthenticationTokenRepository multiFactorAuthenticationTokenRepository, MessageSender messageSender, ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.multiFactorAuthenticationTokenRepository = multiFactorAuthenticationTokenRepository;
        this.messageSender = messageSender;
        this.objectMapper = objectMapper;
    }

    public HttpStatus validateEmail(ValidateEmailRequest validateEmailRequest) {
        MultiFactorAuthenticationTokenEntity multiFactorAuthenticationTokenEntity = multiFactorAuthenticationTokenRepository
                .findById(validateEmailRequest.getId())
                .orElseThrow(() -> new VerificationTokenException("Verification token doesn't exist."));

        if (multiFactorAuthenticationTokenEntity.getExpiryDate().before(new Date())) {
            return HttpStatus.GONE;
        } else if (!multiFactorAuthenticationTokenEntity.getToken().equals(validateEmailRequest.getValidationToken())) {
            return HttpStatus.UNAUTHORIZED;
        } else if (multiFactorAuthenticationTokenEntity.getUserEntity().getEnabled()) {
            return HttpStatus.ALREADY_REPORTED;
        }

        UserEntity userEntity = multiFactorAuthenticationTokenEntity.getUserEntity();
        userEntity.setEnabled(true);
        multiFactorAuthenticationTokenRepository.save(multiFactorAuthenticationTokenEntity);

        return HttpStatus.OK;
    }

    @Transactional
    public String registerUser(RegisterUserRequest registerUserRequest) {
        if (StringUtils.isBlank(registerUserRequest.getPassword())) {
            log.error("Password is empty");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        UserEntity userEntity = saveUserInDB(registerUserRequest);
        String token = UUID.randomUUID().toString();
        Long verificationTokenId = saveVerificationToken(userEntity, token);

        String verificationUrl = prepareVerificationUrl(verificationTokenId, token);
        String recipientAddress = userEntity.getEmail();

        AccountCreationDto accountCreationDto = new AccountCreationDto(recipientAddress, verificationUrl);
        messageSender.sendAccountCreationValidationEmail(accountCreationDto);

        return "User registration successful";
    }

    private static String prepareVerificationUrl(Long verificationTokenId, String token) {
        return SLASH + verificationTokenId + SLASH + token;
    }

    private UserEntity saveUserInDB(RegisterUserRequest registerUserRequest) {

        UserEntity userEntity = objectMapper.destinationToSource(registerUserRequest);
        userEntity.setPassword(B_CRYPT_PASSWORD_ENCODER.encode(registerUserRequest.getPassword()));
        userEntity.setEnabled(false);

        return userRepository.save(userEntity);
    }

    private Long saveVerificationToken(UserEntity userEntity, String token) {
        MultiFactorAuthenticationTokenEntity myToken = new MultiFactorAuthenticationTokenEntity();
        myToken.setToken(token);
        myToken.setExpiryDate(calculateExpiryDate());
        myToken.setUserEntity(userEntity);

        multiFactorAuthenticationTokenRepository.save(myToken);
        return myToken.getId();
    }

    private Date calculateExpiryDate() {
        Date now = new Date();

        return new Date(now.getTime() + registrationTokenTimeToLive);
    }
}
