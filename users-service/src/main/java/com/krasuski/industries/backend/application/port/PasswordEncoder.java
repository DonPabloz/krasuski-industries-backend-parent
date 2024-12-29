package com.krasuski.industries.backend.application.port;

import com.krasuski.industries.backend.domain.value.Password;

public interface PasswordEncoder {
    Password encode(Password password);

    boolean matches(Password rawPassword, Password encodedPassword);
}
