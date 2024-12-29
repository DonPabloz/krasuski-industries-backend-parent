package com.krasuski.industries.backend.dto.address.response;

import lombok.Value;

import java.util.UUID;

@Value
public class PrivateAddressResponse {
    String publicId;
    String firstName;
    String lastName;
    String email;
    String phoneNumber;
    String street;
    String buildingNumber;
    String apartmentNumber;
    String zipCode;
    String city;
}
