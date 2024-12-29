package com.krasuski.industries.backend.application.command;

import com.krasuski.industries.backend.domain.value.CompanyRecipient;
import com.krasuski.industries.backend.domain.value.PhysicalAddress;
import com.krasuski.industries.backend.domain.value.UserPubId;

public record AddCompanyAddressCommand(
        UserPubId userPubId, PhysicalAddress physicalAddress, CompanyRecipient companyRecipient) {
}
