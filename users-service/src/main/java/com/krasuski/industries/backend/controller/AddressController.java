package com.krasuski.industries.backend.controller;

import com.krasuski.industries.backend.application.*;
import com.krasuski.industries.backend.application.command.AddInpostLockerCommand;
import com.krasuski.industries.backend.application.command.AddPrivateAddressCommand;
import com.krasuski.industries.backend.application.command.RemoveInpostLockerCommand;
import com.krasuski.industries.backend.domain.CompanyAddress;
import com.krasuski.industries.backend.domain.PrivateAddress;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.value.*;
import com.krasuski.industries.backend.dto.address.request.*;
import com.krasuski.industries.backend.dto.address.response.CompanyAddressResponse;
import com.krasuski.industries.backend.dto.address.response.LockerAddressResponse;
import com.krasuski.industries.backend.dto.address.response.PrivateAddressResponse;
import com.krasuski.industries.backend.dto.user.response.ReceiversResponse;
import com.krasuski.industries.backend.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.UUID;

import static com.krasuski.industries.backend.util.CustomHeaders.USER_ID_HEADER_NAME;

@RestController
public class AddressController {

    private final AddressService addressService;
    private final GetUserUseCase getUserUseCase;
    private final AddAddressUseCase addAddressUseCase;
    private final RemoveAddressUseCase removeAddressUseCase;
    private final AddInpostLockerUseCase addInpostLockerUseCase;
    private final RemoveInpostLockerUseCase removeInpostLockerUseCase;

    public AddressController(AddressService addressService, GetUserUseCase getUserUseCase, AddAddressUseCase addAddressUseCase, RemoveAddressUseCase removeAddressUseCase, AddInpostLockerUseCase addInpostLockerUseCase, RemoveInpostLockerUseCase removeInpostLockerUseCase) {
        this.addressService = addressService;
        this.getUserUseCase = getUserUseCase;
        this.addAddressUseCase = addAddressUseCase;
        this.removeAddressUseCase = removeAddressUseCase;
        this.addInpostLockerUseCase = addInpostLockerUseCase;
        this.removeInpostLockerUseCase = removeInpostLockerUseCase;
    }

    @GetMapping(value = "/addresses")
    public ResponseEntity<ReceiversResponse> getAddresses(@RequestHeader(USER_ID_HEADER_NAME) Long userId) {
        User user = getUserUseCase.getUser(new UserPubId(userId.toString()));

        ReceiversResponse receiversResponse = new ReceiversResponse(
                user.getPrivateAddresses().stream().map(this::fromUser).toList(),
                user.getCompanyAddresses().stream().map(this::fromUser).toList(),
                //TODO add call to inpost api in useCase
                new ArrayList<>()
        );

        return ResponseEntity.status(HttpStatus.OK).body(receiversResponse);
    }

    @PostMapping(value = "/addresses/private")
    public ResponseEntity<Void> createPrivateAddress(
            @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @Valid @RequestBody CreatePrivateAddressRequest createPrivateAddressRequest) {
        PhysicalAddress physicalAddress = new PhysicalAddress(
                createPrivateAddressRequest.getStreet(),
                createPrivateAddressRequest.getBuildingNumber().toString(),
                createPrivateAddressRequest.getApartmentNumber().toString(),
                createPrivateAddressRequest.getZipCode(),
                createPrivateAddressRequest.getCity()
        );
        PrivateRecipient privateRecipient = new PrivateRecipient(
                new FirstName(createPrivateAddressRequest.getFirstName()),
                new LastName(createPrivateAddressRequest.getLastName()),
                new Email(createPrivateAddressRequest.getEmail()),
                new PhoneNumber("48", createPrivateAddressRequest.getPhoneNumber())
        );
        AddPrivateAddressCommand addPrivateAddressCommand = new AddPrivateAddressCommand(
                new UserPubId(userId.toString()),
                physicalAddress,
                privateRecipient
        );
        addAddressUseCase.addPrivateAddress(addPrivateAddressCommand);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping(value = "/addresses/private")
    @Deprecated
    public ResponseEntity<PrivateAddressResponse> updatePrivateAddress(
            @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @Valid @RequestBody UpdatePrivateAddressRequest updatePrivateAddressRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(addressService.updatePrivateAddress(userId, updatePrivateAddressRequest));
    }

    @DeleteMapping(value = "/addresses/private")
    public ResponseEntity<Void> deletePrivateAddress(
            @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @RequestParam UUID publicId) {
        removeAddressUseCase.removePrivateAddress(
                new UserPubId(userId.toString()),
                new AddressPubId(publicId.toString())
        );
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "/addresses/company")
    public ResponseEntity<CompanyAddressResponse> createCompanyAddress(
            @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @Valid @RequestBody CreateCompanyAddressRequest createCompanyAddressRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(addressService.createCompanyAddress(userId, createCompanyAddressRequest));
    }

    @PatchMapping(value = "/addresses/company")
    @Deprecated
    public ResponseEntity<CompanyAddressResponse> updateCompanyAddress(
            @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @Valid @RequestBody UpdateCompanyAddressRequest updateCompanyAddressRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(addressService.updateCompanyAddress(userId, updateCompanyAddressRequest));
    }

    @DeleteMapping(value = "/addresses/company")
    public ResponseEntity<Void> deleteCompanyAddress(
            @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @RequestParam UUID publicId) {
        removeAddressUseCase.removeCompanyAddress(
                new UserPubId(userId.toString()),
                new AddressPubId(publicId.toString())
        );
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping(value = "/addresses/locker")
    public ResponseEntity<Void> createLockerAddress(
            @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @Valid @RequestBody CreateLockerAddressRequest createLockerAddressRequest) {
        AddInpostLockerCommand addInpostLockerCommand = new AddInpostLockerCommand(
                new UserPubId(userId.toString()),
                new InpostExternalPubId(createLockerAddressRequest.getExternalId())
        );
        addInpostLockerUseCase.addInpostLocker(addInpostLockerCommand);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping(value = "/addresses/locker")
    @Deprecated
    public ResponseEntity<LockerAddressResponse> updateLockerAddress(
            @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @Valid @RequestBody UpdateLockerAddressRequest updateLockerAddressRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(addressService.updateLockerAddress(userId, updateLockerAddressRequest));
    }

    @DeleteMapping(value = "/addresses/locker")
    public ResponseEntity<Void> deleteLockerAddress(
            @RequestHeader(USER_ID_HEADER_NAME) Long userId,
            @RequestParam UUID publicId) {
        RemoveInpostLockerCommand removeInpostLockerCommand = new RemoveInpostLockerCommand(
                new UserPubId(userId.toString()),
                new InpostExternalPubId(publicId.toString())
        );
        removeInpostLockerUseCase.removeInpostLocker(removeInpostLockerCommand);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private PrivateAddressResponse fromUser(PrivateAddress privateAddress) {
        return new PrivateAddressResponse(
                privateAddress.getPubId().value(),
                privateAddress.getPrivateRecipient().firstName().value(),
                privateAddress.getPrivateRecipient().lastName().value(),
                privateAddress.getPrivateRecipient().email().value(),
                privateAddress.getPrivateRecipient().phoneNumber().number(),
                privateAddress.getPhysicalAddress().street(),
                privateAddress.getPhysicalAddress().buildingNumber(),
                privateAddress.getPhysicalAddress().apartmentNumber(),
                privateAddress.getPhysicalAddress().zipCode(),
                privateAddress.getPhysicalAddress().city()
        );
    }

    private CompanyAddressResponse fromUser(CompanyAddress companyAddress) {
        return new CompanyAddressResponse(
                companyAddress.getPubId().value(),
                companyAddress.getCompanyRecipient().companyName().value(),
                companyAddress.getCompanyRecipient().companyNIP().value(),
                companyAddress.getCompanyRecipient().email().value(),
                companyAddress.getCompanyRecipient().phoneNumber().number(),
                companyAddress.getPhysicalAddress().street(),
                companyAddress.getPhysicalAddress().buildingNumber(),
                companyAddress.getPhysicalAddress().apartmentNumber(),
                companyAddress.getPhysicalAddress().zipCode(),
                companyAddress.getPhysicalAddress().city()
        );
    }
}
