package com.krasuski.industries.backend;

import com.krasuski.industries.backend.entity.UserEntity;
import com.krasuski.industries.backend.entity.UserRole;
import com.krasuski.industries.backend.entity.address.CompanyAddress;
import com.krasuski.industries.backend.entity.address.LockerAddress;
import com.krasuski.industries.backend.entity.address.PrivateAddress;
import com.krasuski.industries.backend.enums.Role;
import com.krasuski.industries.backend.util.PasswordEncoder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractUnitTest {

    protected static final BCryptPasswordEncoder B_CRYPT_PASSWORD_ENCODER = PasswordEncoder.getInstance();
    protected static final String PASSWORD = "password";

    protected final UserEntity userWithAddresses = new UserEntity();
    protected final UserEntity userWithoutAddresses = new UserEntity();

    protected AbstractUnitTest() {
        userWithAddresses.setId(1L);
        userWithoutAddresses.setId(11L);
    }

    protected UserEntity getAdminUser() {
        UserRole userRole = new UserRole();
        userRole.setRole(Role.ADMIN);

        UserEntity userEntity = getUserEntity();
        userEntity.setUserRole(userRole);

        return userEntity;
    }

    protected UserEntity getRegularUser() {
        UserRole userRole = new UserRole();
        userRole.setRole(Role.USER);

        UserEntity userEntity = getUserEntity();
        userEntity.setUserRole(userRole);

        return userEntity;
    }

    protected CompanyAddress getCompanyAddress() {
        CompanyAddress companyAddress = new CompanyAddress();
        companyAddress.setCompanyName("TestCompany");
        companyAddress.setCompanyNIP("1234567890");
        companyAddress.setEmail("");
        companyAddress.setPhoneExtension("");
        companyAddress.setPhoneNumber("");
        companyAddress.setCity("TestCity");
        companyAddress.setZipCode("12-345");
        companyAddress.setStreet("TestStreet");
        companyAddress.setBuildingNumber(1);
        companyAddress.setApartmentNumber(1);
        companyAddress.setUser(userWithAddresses);

        return companyAddress;
    }

    protected PrivateAddress getPrivateAddress() {
        PrivateAddress privateAddress = new PrivateAddress();
        privateAddress.setFirstName("TestFirstName");
        privateAddress.setLastName("TestLastName");
        privateAddress.setEmail("");
        privateAddress.setPhoneExtension("");
        privateAddress.setPhoneNumber("");
        privateAddress.setCity("TestCity");
        privateAddress.setZipCode("12-345");
        privateAddress.setStreet("TestStreet");
        privateAddress.setBuildingNumber(1);
        privateAddress.setApartmentNumber(1);
        privateAddress.setUser(userWithAddresses);

        return privateAddress;
    }

    protected LockerAddress getLockerAddress() {
        LockerAddress lockerAddress = new LockerAddress();
        lockerAddress.setPublicId(UUID.randomUUID());
        lockerAddress.setName("TestLocker");
        lockerAddress.setExternalId("1234567890");
        lockerAddress.setCity("TestCity");
        lockerAddress.setZipCode("12-345");
        lockerAddress.setStreet("TestStreet");
        lockerAddress.setBuildingNumber(1);
        lockerAddress.setUser(userWithAddresses);

        return lockerAddress;
    }


    private UserEntity getUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEnabled(true);
        userEntity.setPassword(B_CRYPT_PASSWORD_ENCODER.encode(PASSWORD));
        userEntity.setPublicId(UUID.randomUUID());
        userEntity.setEmail("someEmail@gmail.com");

        return userEntity;
    }
}
