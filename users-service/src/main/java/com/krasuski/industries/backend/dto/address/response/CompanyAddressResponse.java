package com.krasuski.industries.backend.dto.address.response;

import lombok.Value;

import java.util.UUID;

@Value
public class CompanyAddressResponse {
    String publicId;
    String companyName;
    String companyNIP;
    String email;
    String phoneNumber;
    String street;
    String buildingNumber;
    String apartmentNumber;
    String zipCode;
    String city;
}
