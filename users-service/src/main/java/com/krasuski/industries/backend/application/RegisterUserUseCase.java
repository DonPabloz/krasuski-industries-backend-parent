package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.RegisterUserCommand;
import com.krasuski.industries.backend.application.port.PasswordEncoder;
import com.krasuski.industries.backend.application.port.UserRepository;
import com.krasuski.industries.backend.application.port.VerificationTokenRepository;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.VerificationToken;
import com.krasuski.industries.backend.domain.exception.UserException;
import com.krasuski.industries.backend.domain.value.Email;
import com.krasuski.industries.backend.domain.value.Password;
import com.krasuski.industries.backend.dto.account.AccountCreationDto;
import com.krasuski.industries.backend.messaging.MessageSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class RegisterUserUseCase {

    private static final String SLASH = "/";

    private final MessageSender messageSender;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(MessageSender messageSender, UserRepository userRepository, VerificationTokenRepository verificationTokenRepository, PasswordEncoder passwordEncoder) {
        this.messageSender = messageSender;
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(RegisterUserCommand registerUserCommand) {
        Email email = registerUserCommand.email();
        Password hashedPassword = passwordEncoder.encode(registerUserCommand.password());

        if (userRepository.findByEmail(email).isPresent()) {
            log.error("User with email: {} already exists.", email);
            throw new UserException("User with email: " + email + " already exists.");
        }

        User user = User.createRegisteredUser(email, hashedPassword);
        userRepository.save(user);
        log.info("User with email: {} registered.", email);

        VerificationToken verificationToken = VerificationToken.createNew(user, 60 * 24);
        verificationTokenRepository.save(verificationToken);
        log.info("Verification token created for user with email: {}", email);

        AccountCreationDto accountCreationDto = new AccountCreationDto(email.value(), prepareVerificationUrl(user.getPubId().toString(), verificationToken.getToken()));
        messageSender.sendAccountCreationValidationEmail(accountCreationDto);
    }

    private String prepareVerificationUrl(String userPubId, String token) {
        return SLASH + userPubId + SLASH + token;
    }
}
