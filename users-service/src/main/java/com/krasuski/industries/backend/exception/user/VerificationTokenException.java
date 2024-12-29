package com.krasuski.industries.backend.exception.user;

public class VerificationTokenException extends RuntimeException {
    public VerificationTokenException(String message) {
        super(message);
    }
}
