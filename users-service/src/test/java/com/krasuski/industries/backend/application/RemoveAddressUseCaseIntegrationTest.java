package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.domain.CompanyAddress;
import com.krasuski.industries.backend.domain.PrivateAddress;
import com.krasuski.industries.backend.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class RemoveAddressUseCaseIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private RemoveAddressUseCase removeAddressUseCase;

    @Test
    void removePrivateAddress_userAndAddressExists_privateAddressRemoved() {
        //given
        User user = createUser();
        PrivateAddress privateAddress = user.getPrivateAddresses().get(0);

        //when
        removeAddressUseCase.removePrivateAddress(user.getPubId(), privateAddress.getPubId());

        //then
        User savedUser = userRepository.findByPubId(user.getPubId()).get();
        Optional<PrivateAddress> removedAddressOptional = savedUser.getPrivateAddresses()
                .stream()
                .filter(pa -> pa.getId().equals(privateAddress.getId()))
                .findAny();
        assertThat(removedAddressOptional).isEmpty();
    }

    @Test
    void removeCompanyAddress_userAndAddressExists_companyAddressRemoved() {
        //given
        User user = createUser();
        CompanyAddress companyAddress = user.getCompanyAddresses().get(0);

        //when
        removeAddressUseCase.removeCompanyAddress(user.getPubId(), companyAddress.getPubId());

        //then
        User savedUser = userRepository.findByPubId(user.getPubId()).get();
        Optional<CompanyAddress> removedAddressOptional = savedUser.getCompanyAddresses()
                .stream()
                .filter(pa -> pa.getId().equals(companyAddress.getId()))
                .findAny();
        assertThat(removedAddressOptional).isEmpty();
    }
}