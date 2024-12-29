package com.krasuski.industries.backend.domain.value;

public record PhysicalAddress(
        String street,
        String buildingNumber,
        String apartmentNumber,
        String zipCode,
        String city
) {
}
