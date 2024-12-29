package com.krasuski.industries.backend.domain;

import com.krasuski.industries.backend.domain.value.AddressPubId;
import com.krasuski.industries.backend.domain.value.CompanyRecipient;
import com.krasuski.industries.backend.domain.value.PhysicalAddress;
import com.krasuski.industries.backend.domain.value.UserPubId;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@Getter
@Setter
public class CompanyAddress {

    private final AddressPubId pubId;
    private final Long userId;

    private Long id;
    private CompanyRecipient companyRecipient;
    private PhysicalAddress physicalAddress;

    public CompanyAddress(Long userId, PhysicalAddress physicalAddress, CompanyRecipient companyRecipient, AddressPubId pubId) {
        this.userId = userId;
        this.pubId = pubId;
        this.physicalAddress = physicalAddress;
        this.companyRecipient = companyRecipient;
    }

    public static CompanyAddress create(Long userId, PhysicalAddress physicalAddress, CompanyRecipient companyRecipient) {
        return new CompanyAddress(userId, physicalAddress, companyRecipient, generatePubId());
    }

    private static AddressPubId generatePubId() {
        return new AddressPubId(UUID.randomUUID().toString());
    }

    public void update(UserPubId userPubId, PhysicalAddress physicalAddress, CompanyRecipient companyRecipient) {
        if (this.userId.equals(userPubId)) {
            this.physicalAddress = physicalAddress;
            this.companyRecipient = companyRecipient;
        } else {
            log.error("User with pubId {} tried to update address with pubId {}.", userPubId, this.pubId);
            throw new IllegalArgumentException("UserPubId does not match");
        }
    }
}
