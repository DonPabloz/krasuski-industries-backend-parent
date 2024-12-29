package com.krasuski.industries.backend.domain.value;

public record CompanyRecipient(
        CompanyName companyName,
        CompanyNIP companyNIP,
        Email email,
        PhoneNumber phoneNumber
) {
}
