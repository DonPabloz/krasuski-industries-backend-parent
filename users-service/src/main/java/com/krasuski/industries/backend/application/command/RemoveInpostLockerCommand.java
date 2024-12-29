package com.krasuski.industries.backend.application.command;

import com.krasuski.industries.backend.domain.value.InpostExternalPubId;
import com.krasuski.industries.backend.domain.value.UserPubId;

public record RemoveInpostLockerCommand(
        UserPubId userPubId,
        InpostExternalPubId inpostExternalPubId
) {
}
