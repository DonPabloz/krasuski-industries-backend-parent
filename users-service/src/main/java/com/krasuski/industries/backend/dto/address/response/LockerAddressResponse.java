package com.krasuski.industries.backend.dto.address.response;

import lombok.Value;

import java.util.UUID;

@Value
public class LockerAddressResponse {
    UUID publicId;
    String name;
    String externalId;
    String street;
    Integer buildingNumber;
    String zipCode;
    String city;
}
