package com.krasuski.industries.backend.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaPasswordResetTokenRepository extends JpaRepository<DDDPasswordResetTokenEntity, Long> {
    Optional<DDDPasswordResetTokenEntity> findByToken(String token);
}
