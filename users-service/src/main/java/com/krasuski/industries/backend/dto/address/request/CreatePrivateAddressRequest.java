package com.krasuski.industries.backend.dto.address.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreatePrivateAddressRequest {

    @NotBlank(message = "FirstName is mandatory")
    private String firstName;
    @NotBlank(message = "LastName is mandatory")
    private String lastName;
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "PhoneExtension is mandatory")
    private String phoneExtension;
    @NotBlank(message = "PhoneNumber is mandatory")
    private String phoneNumber;
    @NotBlank(message = "Street is mandatory")
    private String street;
    @NotBlank(message = "BuildingNumber is mandatory")
    private Integer buildingNumber;
    @NotBlank(message = "ApartmentNumber is mandatory")
    private Integer apartmentNumber;
    @NotBlank(message = "ZipCode is mandatory")
    private String zipCode;
    @NotBlank(message = "City is mandatory")
    private String city;
}
