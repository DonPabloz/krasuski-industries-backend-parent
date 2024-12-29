package com.krasuski.industries.backend.repositories.user;

import com.krasuski.industries.backend.entity.MultiFactorAuthenticationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultiFactorAuthenticationTokenRepository extends JpaRepository<MultiFactorAuthenticationTokenEntity, Long> {

}
