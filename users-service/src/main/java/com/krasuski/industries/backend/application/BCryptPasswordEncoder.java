package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.port.PasswordEncoder;
import com.krasuski.industries.backend.domain.value.Password;
import org.springframework.stereotype.Service;

@Service
class BCryptPasswordEncoder implements PasswordEncoder {

    private static final org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = com.krasuski.industries.backend.util.PasswordEncoder.getInstance();

    @Override
    public Password encode(Password password) {
        return new Password(B_CRYPT_PASSWORD_ENCODER.encode(password.value()));
    }

    @Override
    public boolean matches(Password rawPassword, Password encodedPassword) {
        return B_CRYPT_PASSWORD_ENCODER.matches(rawPassword.value(), encodedPassword.value());
    }
}
