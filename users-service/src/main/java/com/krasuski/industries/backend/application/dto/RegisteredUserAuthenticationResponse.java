package com.krasuski.industries.backend.application.dto;

import com.krasuski.industries.backend.domain.UserRole;
import com.krasuski.industries.backend.domain.value.AccessTokenValue;
import com.krasuski.industries.backend.domain.value.RefreshTokenValue;
import com.krasuski.industries.backend.domain.value.UserPubId;

public record RegisteredUserAuthenticationResponse(
        AccessTokenValue accessToken,
        RefreshTokenValue refreshToken,
        UserPubId userPubId,
        UserRole userRole
) {
}
