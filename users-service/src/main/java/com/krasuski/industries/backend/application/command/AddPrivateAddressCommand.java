package com.krasuski.industries.backend.application.command;

import com.krasuski.industries.backend.domain.value.PhysicalAddress;
import com.krasuski.industries.backend.domain.value.PrivateRecipient;
import com.krasuski.industries.backend.domain.value.UserPubId;

public record AddPrivateAddressCommand(
        UserPubId userPubId, PhysicalAddress physicalAddress, PrivateRecipient privateRecipient
) {
}
