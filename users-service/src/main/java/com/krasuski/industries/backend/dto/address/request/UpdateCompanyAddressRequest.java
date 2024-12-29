package com.krasuski.industries.backend.dto.address.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateCompanyAddressRequest {

    @NotBlank(message = "PublicId is mandatory")
    private UUID publicId;

    @NotBlank(message = "Company name is mandatory")
    private String companyName;

    @NotBlank(message = "Company NIP is mandatory")
    private String companyNIP;

    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;

    @NotBlank(message = "Street is mandatory")
    private String street;

    @NotNull(message = "Building number is mandatory")
    private Integer buildingNumber;

    @NotNull(message = "Apartment number is mandatory")
    private Integer apartmentNumber;

    @NotBlank(message = "Zip code is mandatory")
    private String zipCode;

    @NotBlank(message = "City is mandatory")
    private String city;
}
