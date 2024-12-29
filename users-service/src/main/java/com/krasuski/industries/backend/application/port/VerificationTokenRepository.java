package com.krasuski.industries.backend.application.port;

import com.krasuski.industries.backend.domain.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository {

    void save(VerificationToken verificationToken);

    Optional<VerificationToken> findByToken(String token);
}
