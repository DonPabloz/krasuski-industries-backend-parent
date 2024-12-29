package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.port.UserRepository;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.value.InpostExternalPubId;
import com.krasuski.industries.backend.factory.AddressTestDataFactory;
import com.krasuski.industries.backend.factory.UserTestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
abstract class AbstractIntegrationTest {

    @Autowired
    UserRepository userRepository;

    User createUser() {
        User user = new UserTestDataFactory.UserBuilder().withId(null).buildRegisteredUser();

        user.getPrivateAddresses().add(AddressTestDataFactory.aPrivateAddress(user.getId()));
        user.getPrivateAddresses().add(AddressTestDataFactory.aPrivateAddress(user.getId()));

        user.getCompanyAddresses().add(AddressTestDataFactory.aCompanyAddress(user.getId()));
        user.getCompanyAddresses().add(AddressTestDataFactory.aCompanyAddress(user.getId()));

        user.getInpostLockers().add(new InpostExternalPubId(UUID.randomUUID().toString()));

        userRepository.save(user);

        return user;
    }

    User createRegisteredUserWithoutAddresses() {
        User user = UserTestDataFactory.aRegisteredUser();
        userRepository.save(user);

        return user;
    }
}
