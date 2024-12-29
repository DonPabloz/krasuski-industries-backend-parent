package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.AddCompanyAddressCommand;
import com.krasuski.industries.backend.application.command.AddPrivateAddressCommand;
import com.krasuski.industries.backend.application.port.UserRepository;
import com.krasuski.industries.backend.domain.CompanyAddress;
import com.krasuski.industries.backend.domain.PrivateAddress;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.value.CompanyRecipient;
import com.krasuski.industries.backend.domain.value.PhysicalAddress;
import com.krasuski.industries.backend.domain.value.PrivateRecipient;
import com.krasuski.industries.backend.domain.value.UserPubId;
import com.krasuski.industries.backend.factory.AddressTestDataFactory;
import com.krasuski.industries.backend.factory.UserTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

class AddAddressUseCaseIntegrationTest extends AbstractIntegrationTest{

    @Autowired
    private AddAddressUseCase addAddressUseCase;

    @Test
    void addPrivateAddress_properDataPassed_privateAddressAdded() {
        //given
        User user = createUserWithoutAddresses();
        PhysicalAddress physicalAddress = AddressTestDataFactory.aPhysicalAddress();
        PrivateRecipient privateRecipient = AddressTestDataFactory.aPrivateRecipient();
        AddPrivateAddressCommand addPrivateAddressCommand = new AddPrivateAddressCommand(
                user.getPubId(),
                physicalAddress,
                privateRecipient
        );

        //when
        addAddressUseCase.addPrivateAddress(addPrivateAddressCommand);

        //then
        User userWithPrivateAddress = userRepository.findByPubId(user.getPubId()).orElseThrow();
        assertThat(userWithPrivateAddress.getPrivateAddresses()).hasSize(1);

        PrivateAddress savedPrivateAddress = userWithPrivateAddress.getPrivateAddresses().get(0);
        assertThat(savedPrivateAddress.getPhysicalAddress()).isEqualTo(physicalAddress);
        assertThat(savedPrivateAddress.getPrivateRecipient()).isEqualTo(privateRecipient);
    }

    @Test
    void addCompanyAddress_properDataPassed_companyAddressAdded() {
        //given
        User user = createUserWithoutAddresses();
        PhysicalAddress physicalAddress = AddressTestDataFactory.aPhysicalAddress();
        CompanyRecipient companyRecipient = AddressTestDataFactory.aCompanyRecipient();
        AddCompanyAddressCommand addCompanyAddressCommand = new AddCompanyAddressCommand(
                user.getPubId(),
                physicalAddress,
                companyRecipient
        );

        //when
        addAddressUseCase.addCompanyAddress(addCompanyAddressCommand);

        //then
        User userWithCompanyAddress = userRepository.findByPubId(user.getPubId()).orElseThrow();
        assertThat(userWithCompanyAddress.getCompanyAddresses()).hasSize(1);

        CompanyAddress savedCompanyAddress = userWithCompanyAddress.getCompanyAddresses().get(0);
        assertThat(savedCompanyAddress.getPhysicalAddress()).isEqualTo(physicalAddress);
        assertThat(savedCompanyAddress.getCompanyRecipient()).isEqualTo(companyRecipient);
    }

    private User createUserWithoutAddresses() {
        User user = UserTestDataFactory.aRegisteredUser();
        userRepository.save(user);

        return user;
    }
}