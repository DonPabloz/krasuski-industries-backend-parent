package com.krasuski.industries.backend.repositories.user;

import com.krasuski.industries.backend.entity.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {

    List<UserRefreshToken> findAllByUserEntityId(Long userDetailsEntityId);
}
