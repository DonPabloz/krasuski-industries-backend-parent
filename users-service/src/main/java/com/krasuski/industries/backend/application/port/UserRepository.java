package com.krasuski.industries.backend.application.port;

import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.value.Email;
import com.krasuski.industries.backend.domain.value.UserPubId;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByPubId(UserPubId pubId);

    Optional<User> findByEmail(Email email);

    void save(User user);
}
