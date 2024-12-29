package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.port.UserRepository;
import com.krasuski.industries.backend.domain.User;
import com.krasuski.industries.backend.domain.value.AddressPubId;
import com.krasuski.industries.backend.domain.value.UserPubId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RemoveAddressUseCase {

    private final UserRepository userRepository;

    public RemoveAddressUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void removePrivateAddress(UserPubId userPubId, AddressPubId addressPubId) {
        User user = UserUtil.findUserByPubId(userRepository, userPubId);
        user.removePrivateAddress(addressPubId);
        userRepository.save(user);
        log.info("Private address removed for user with pubId: {}", userPubId);
    }

    public void removeCompanyAddress(UserPubId userPubId, AddressPubId addressPubId) {
        User user = UserUtil.findUserByPubId(userRepository, userPubId);
        user.removeCompanyAddress(addressPubId);
        userRepository.save(user);
        log.info("Company address removed for user with pubId: {}", userPubId);
    }
}
