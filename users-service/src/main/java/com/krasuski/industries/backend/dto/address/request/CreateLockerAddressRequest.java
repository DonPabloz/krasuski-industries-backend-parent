package com.krasuski.industries.backend.dto.address.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateLockerAddressRequest {

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "External ID is mandatory")
    private String externalId;

    @NotBlank(message = "Street is mandatory")
    private String street;

    @NotNull(message = "Building number is mandatory")
    private Integer buildingNumber;

    @NotBlank(message = "Zip code is mandatory")
    private String zipCode;

    @NotBlank(message = "City is mandatory")
    private String city;
}