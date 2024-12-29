package com.krasuski.industries.backend.factory;

import com.krasuski.industries.backend.domain.CompanyAddress;
import com.krasuski.industries.backend.domain.PrivateAddress;
import com.krasuski.industries.backend.domain.value.*;
import net.datafaker.Faker;

public class AddressTestDataFactory {

    private static final Faker FAKER = new Faker();

    public static PhysicalAddress aPhysicalAddress() {
        return new PhysicalAddress(
                FAKER.address().streetAddress(),
                FAKER.address().streetAddressNumber(),
                FAKER.address().buildingNumber(),
                FAKER.address().zipCode(),
                FAKER.address().city()
        );
    }

    public static PrivateRecipient aPrivateRecipient() {
        return new PrivateRecipient(
                new FirstName(FAKER.name().firstName()),
                new LastName(FAKER.name().lastName()),
                new Email(FAKER.internet().emailAddress()),
                new PhoneNumber(FAKER.phoneNumber().extension(), FAKER.phoneNumber().cellPhone())
        );
    }

    public static CompanyRecipient aCompanyRecipient() {
        return new CompanyRecipient(
                new CompanyName(FAKER.company().name()),
                new CompanyNIP(FAKER.number().digits(10)),
                new Email(FAKER.internet().emailAddress()),
                new PhoneNumber(FAKER.phoneNumber().extension(), FAKER.phoneNumber().cellPhone())
        );
    }

    public static PrivateAddress aPrivateAddress(Long userId) {
        return new PrivateAddress(
                userId,
                aPhysicalAddress(),
                aPrivateRecipient(),
                new AddressPubId(FAKER.internet().uuid())
        );
    }

    public static CompanyAddress aCompanyAddress(Long userId) {
        return new CompanyAddress(
                userId,
                aPhysicalAddress(),
                aCompanyRecipient(),
                new AddressPubId(FAKER.internet().uuid())
        );
    }
}
