package com.krasuski.industries.backend.domain;

import com.krasuski.industries.backend.domain.value.AddressPubId;
import com.krasuski.industries.backend.domain.value.PhysicalAddress;
import com.krasuski.industries.backend.domain.value.PrivateRecipient;
import com.krasuski.industries.backend.domain.value.UserPubId;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Getter
@Setter
public class PrivateAddress {

    private final AddressPubId pubId;
    private final Long userId;

    private Long id;
    private PrivateRecipient privateRecipient;
    private PhysicalAddress physicalAddress;

    public PrivateAddress(Long userId, PhysicalAddress physicalAddress, PrivateRecipient privateRecipient, AddressPubId pubId) {
        this.userId = userId;
        this.pubId = pubId;
        this.physicalAddress = physicalAddress;
        this.privateRecipient = privateRecipient;
    }

    public static PrivateAddress create(Long userId, PhysicalAddress physicalAddress, PrivateRecipient privateRecipient) {
        return new PrivateAddress(userId, physicalAddress, privateRecipient, generatePubId());
    }

    public void update(UserPubId userPubId, PhysicalAddress physicalAddress, PrivateRecipient privateRecipient) {
        if (this.userId.equals(userPubId)) {
            this.physicalAddress = physicalAddress;
            this.privateRecipient = privateRecipient;
        } else {
            log.error("User with pubId {} tried to update address with pubId {}.", userPubId, this.pubId);
            throw new IllegalArgumentException("UserPubId does not match");
        }
    }

    private static AddressPubId generatePubId() {
        return new AddressPubId(UUID.randomUUID().toString());
    }
}
