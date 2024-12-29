package com.krasuski.industries.backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//TODO change to enum singleton - safer
public class PasswordEncoder {

    private static volatile BCryptPasswordEncoder instance = null;

    private PasswordEncoder() {
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to create");
        }
    }

    public static BCryptPasswordEncoder getInstance() {
        if (instance == null) {
            synchronized (PasswordEncoder.class) {
                if (instance == null) {
                    instance = new BCryptPasswordEncoder();
                }
            }
        }

        return instance;
    }
}
