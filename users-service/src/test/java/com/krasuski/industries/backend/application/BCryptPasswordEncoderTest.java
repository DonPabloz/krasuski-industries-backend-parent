package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.port.PasswordEncoder;
import com.krasuski.industries.backend.domain.value.Password;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BCryptPasswordEncoderTest {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Test
    void encode_shouldEncodePassword() {
        //given
        Password password = new Password("mysecretpassword");

        //when
        Password encodedPassword = PASSWORD_ENCODER.encode(password);

        //then
        assertNotEquals(password, encodedPassword);
    }

    @Test
    void matches_samePasswordProvided_shouldMatch() {
        //given
        Password password = new Password("mysecretpassword");
        Password encodedPassword = PASSWORD_ENCODER.encode(password);

        //when
        boolean result = PASSWORD_ENCODER.matches(password, encodedPassword);

        //then
        assertTrue(result);
    }

    @Test
    void matches_differentPasswordProvided_shouldNotMatch() {
        //given
        Password password = new Password("mysecretpassword");
        Password encodedPassword = PASSWORD_ENCODER.encode(password);

        //when
        boolean result = PASSWORD_ENCODER.matches(new Password("anothersecretpassword"), encodedPassword);

        //then
        assertFalse(result);
    }
}