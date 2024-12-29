package com.krasuski.industries.backend.application.command;

import com.krasuski.industries.backend.domain.value.AccessTokenValue;
import com.krasuski.industries.backend.domain.value.RefreshTokenValue;

public record TokenAuthCommand(AccessTokenValue accessTokenValue, RefreshTokenValue refreshTokenValue) {
}
