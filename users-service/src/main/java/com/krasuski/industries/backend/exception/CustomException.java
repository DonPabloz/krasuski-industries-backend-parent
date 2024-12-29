package com.krasuski.industries.backend.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record CustomException(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
