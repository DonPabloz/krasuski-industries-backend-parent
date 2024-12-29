package com.krasuski.industries.backend.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface JpaVerificationTokenRepository extends JpaRepository<DDDVerificationTokenEntity, Long> {
    Optional<DDDVerificationTokenEntity> findByToken(String token);
}
