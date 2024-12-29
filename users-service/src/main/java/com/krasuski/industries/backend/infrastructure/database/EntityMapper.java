package com.krasuski.industries.backend.infrastructure.database;

import com.krasuski.industries.backend.domain.*;
import com.krasuski.industries.backend.domain.value.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
interface EntityMapper {
    User fromEntity(DDDUserEntity DDDUserEntity);

    @Mapping(source = "pubId", target = "pubId.value")
    @Mapping(source = "companyName", target = "companyRecipient.companyName")
    @Mapping(source = "companyNIP", target = "companyRecipient.companyNIP")
    @Mapping(source = "email", target = "companyRecipient.email")
    @Mapping(source = "phoneNumber", target = "companyRecipient.phoneNumber.number")
    @Mapping(source = "phoneNumberExtension", target = "companyRecipient.phoneNumber.extension")
    @Mapping(source = "street", target = "physicalAddress.street")
    @Mapping(source = "buildingNumber", target = "physicalAddress.buildingNumber")
    @Mapping(source = "apartmentNumber", target = "physicalAddress.apartmentNumber")
    @Mapping(source = "zipCode", target = "physicalAddress.zipCode")
    @Mapping(source = "city", target = "physicalAddress.city")
    @Mapping(source = "user.id", target = "userId")
    CompanyAddress fromEntity(DDDCompanyAddressEntity DDDCompanyAddressEntity);

    @Mapping(source = "pubId", target = "pubId.value")
    @Mapping(source = "firstName", target = "privateRecipient.firstName")
    @Mapping(source = "lastName", target = "privateRecipient.lastName")
    @Mapping(source = "email", target = "privateRecipient.email")
    @Mapping(source = "phoneNumber", target = "privateRecipient.phoneNumber.number")
    @Mapping(source = "phoneNumberExtension", target = "privateRecipient.phoneNumber.extension")
    @Mapping(source = "street", target = "physicalAddress.street")
    @Mapping(source = "buildingNumber", target = "physicalAddress.buildingNumber")
    @Mapping(source = "apartmentNumber", target = "physicalAddress.apartmentNumber")
    @Mapping(source = "zipCode", target = "physicalAddress.zipCode")
    @Mapping(source = "city", target = "physicalAddress.city")
    @Mapping(source = "user.id", target = "userId")
    PrivateAddress fromEntity(DDDPrivateAddressEntity DDDPrivateAddressEntity);

    @Mapping(source = "inpostExternalPubId", target = "value")
    InpostExternalPubId fromEntity(DDDInpostLockerEntity DDDInpostLockerEntity);

    RefreshToken fromEntity(DDDRefreshTokenEntity DDDRefreshTokenEntity);

    DDDRefreshTokenEntity toEntity(RefreshToken refreshToken);

    DDDUserEntity toEntity(User user);

    @Mapping(source = "pubId.value", target = "pubId")
    @Mapping(source = "privateRecipient.firstName", target = "firstName")
    @Mapping(source = "privateRecipient.lastName", target = "lastName")
    @Mapping(source = "privateRecipient.email", target = "email")
    @Mapping(source = "privateRecipient.phoneNumber.extension", target = "phoneNumberExtension")
    @Mapping(source = "privateRecipient.phoneNumber.number", target = "phoneNumber")
    @Mapping(source = "physicalAddress.street", target = "street")
    @Mapping(source = "physicalAddress.buildingNumber", target = "buildingNumber")
    @Mapping(source = "physicalAddress.apartmentNumber", target = "apartmentNumber")
    @Mapping(source = "physicalAddress.zipCode", target = "zipCode")
    @Mapping(source = "physicalAddress.city", target = "city")
    @Mapping(source = "userId", target = "user.id")
    DDDPrivateAddressEntity toEntity(PrivateAddress privateAddress);

    @Mapping(source = "pubId.value", target = "pubId")
    @Mapping(source = "companyRecipient.companyName", target = "companyName")
    @Mapping(source = "companyRecipient.companyNIP", target = "companyNIP")
    @Mapping(source = "companyRecipient.email", target = "email")
    @Mapping(source = "companyRecipient.phoneNumber.extension", target = "phoneNumberExtension")
    @Mapping(source = "companyRecipient.phoneNumber.number", target = "phoneNumber")
    @Mapping(source = "physicalAddress.street", target = "street")
    @Mapping(source = "physicalAddress.buildingNumber", target = "buildingNumber")
    @Mapping(source = "physicalAddress.apartmentNumber", target = "apartmentNumber")
    @Mapping(source = "physicalAddress.zipCode", target = "zipCode")
    @Mapping(source = "physicalAddress.city", target = "city")
    @Mapping(source = "userId", target = "user.id")
    DDDCompanyAddressEntity toEntity(CompanyAddress companyAddress);

    DDDInpostLockerEntity toEntity(InpostExternalPubId inpostExternalPubId);

    DDDPasswordResetTokenEntity toEntity(PasswordResetToken passwordResetToken);

    PasswordResetToken fromEntity(DDDPasswordResetTokenEntity DDDPasswordResetTokenEntity);

    VerificationToken fromEntity(DDDVerificationTokenEntity DDDVerificationTokenEntity);

    DDDVerificationTokenEntity toEntity(VerificationToken verificationToken);

    default Password mapPassword(String value) {
        return new Password(value);
    }

    default InpostExternalPubId mapInpostExternalPubId(String value) {
        return new InpostExternalPubId(value);
    }

    default String map(Password password) {
        return password != null ? password.value() : null;
    }

    default String map(AddressPubId addressPubId) {
        return addressPubId != null ? addressPubId.value() : null;
    }

    default AddressPubId mapAddressPubId(String value) {
        return new AddressPubId(value);
    }

    //firstName
    default String map(FirstName firstName) {
        return firstName != null ? firstName.value() : null;
    }

    default FirstName mapFirstName(String value) {
        return new FirstName(value);
    }

    //lastName
    default String map(LastName lastName) {
        return lastName != null ? lastName.value() : null;
    }

    default LastName mapLastName(String value) {
        return new LastName(value);
    }

    //Email
    default String map(Email email) {
        return email != null ? email.value() : null;
    }

    default Email mapEmail(String value) {
        return new Email(value);
    }

    //PhoneNumber
    default String map(PhoneNumber phoneNumber) {
        return phoneNumber != null ? phoneNumber.extension() + phoneNumber.number() : null;
    }

    default PhoneNumber mapPhoneNumber(String phoneNumber) {
        return new PhoneNumber(null, phoneNumber);
    }

    default PhoneNumber mapPhoneNumber(String phoneNumberExtension, String phoneNumber) {
        return new PhoneNumber(phoneNumberExtension, phoneNumber);
    }

    default CompanyName mapCompanyName(String value) {
        return new CompanyName(value);
    }

    default String map(CompanyName value) {
        return value != null ? value.value() : null;
    }

    default CompanyNIP mapCompanyNIP(String value) {
        return new CompanyNIP(value);
    }

    default String map(CompanyNIP value) {
        return value != null ? value.value() : null;
    }

    default String map(InpostExternalPubId value) {
        return value != null ? value.value() : null;
    }

    default UserPubId map(String value) {
        return new UserPubId(value);
    }

    default String map(UserPubId value) {
        return value != null ? value.value() : null;
    }

    default SessionTokenValue mapSessionToken(String value) {
        return new SessionTokenValue(value);
    }

    default String map(SessionTokenValue value) {
        return value != null ? value.value() : null;
    }

    default String map(PasswordResetTokenValue value) {
        return value != null ? value.value() : null;
    }

    default PasswordResetTokenValue mapPRTV(String value) {
        return value != null ? new PasswordResetTokenValue(value) : null;
    }
}
