package com.krasuski.industries.backend.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends JpaRepository<DDDUserEntity, Long> {
    Optional<DDDUserEntity> findByPubId(String pubId);

    Optional<DDDUserEntity> findByEmail(String email);
}
