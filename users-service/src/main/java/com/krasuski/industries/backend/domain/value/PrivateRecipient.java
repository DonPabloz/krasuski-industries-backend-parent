package com.krasuski.industries.backend.domain.value;

public record PrivateRecipient(
        FirstName firstName,
        LastName lastName,
        Email email,
        PhoneNumber phoneNumber
) {
}
