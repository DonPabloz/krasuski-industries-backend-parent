package com.krasuski.industries.backend.application.command;

import com.krasuski.industries.backend.domain.value.RefreshTokenValue;
import com.krasuski.industries.backend.domain.value.UserPubId;

public record LogoutUserCommand(UserPubId userPubId, RefreshTokenValue refreshTokenValue) {
}
