package com.krasuski.industries.backend.application.command;

import com.krasuski.industries.backend.domain.value.SessionTokenValue;
import com.krasuski.industries.backend.domain.value.UserPubId;

public record AuthenticateAnonUserCommand (UserPubId userPubId, SessionTokenValue sessionToken) {
}
