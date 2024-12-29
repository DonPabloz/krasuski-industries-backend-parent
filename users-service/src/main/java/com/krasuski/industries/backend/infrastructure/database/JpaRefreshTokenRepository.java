package com.krasuski.industries.backend.infrastructure.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaRefreshTokenRepository extends JpaRepository<DDDRefreshTokenEntity, Long> {
    List<DDDRefreshTokenEntity> findAllByUser(DDDUserEntity DDDUserEntity);
}
