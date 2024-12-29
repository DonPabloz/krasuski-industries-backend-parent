package com.krasuski.industries.backend.exception;

import com.krasuski.industries.backend.exception.user.AccountNotActiveException;
import com.krasuski.industries.backend.exception.user.PasswordIncorrectException;
import com.krasuski.industries.backend.exception.user.UserNotFoundException;
import com.krasuski.industries.backend.exception.user.VerificationTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
@Slf4j
public class UserExceptionHandler {

    @ExceptionHandler(value = {UserNotFoundException.class, PasswordIncorrectException.class, VerificationTokenException.class})
    public ResponseEntity<Object> handleUserAuthenticationException(RuntimeException e) {

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        CustomException customException = new CustomException(
                e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("UTC+2")));

        log.info(customException.message(), e);

        return new ResponseEntity<>(customException, httpStatus);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleUserRegistrationException(DataIntegrityViolationException e) {

        HttpStatus httpStatus = HttpStatus.CONFLICT;

        CustomException customException = new CustomException(
                "Email already exists in database.",
                httpStatus,
                ZonedDateTime.now(ZoneId.of("UTC+2")));

        log.info(customException.message(), e);

        return new ResponseEntity<>(customException, httpStatus);
    }

    @ExceptionHandler(value = {AccountNotActiveException.class})
    public ResponseEntity<Object> handleAccountNotActiveException(RuntimeException e) {

        HttpStatus httpStatus = HttpStatus.LOCKED;

        CustomException customException = new CustomException(
                "Account is not active.",
                httpStatus,
                ZonedDateTime.now(ZoneId.of("UTC+2")));

        log.info(customException.message(), e);

        return new ResponseEntity<>(customException, httpStatus);
    }
}
