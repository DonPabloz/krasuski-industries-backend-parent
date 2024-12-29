package com.krasuski.industries.backend.application;

import com.krasuski.industries.backend.application.command.AddCompanyAddressCommand;
import com.krasuski.industries.backend.application.command.AddPrivateAddressCommand;
import com.krasuski.industries.backend.application.port.UserRepository;
import com.krasuski.industries.backend.domain.CompanyAddress;
import com.krasuski.industries.backend.domain.PrivateAddress;
import com.krasuski.industries.backend.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AddAddressUseCase {

    private final UserRepository userRepository;

    public AddAddressUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addPrivateAddress(AddPrivateAddressCommand command) {
        User user = UserUtil.findUserByPubId(userRepository, command.userPubId());
        PrivateAddress privateAddress = PrivateAddress.create(user.getId(), command.physicalAddress(), command.privateRecipient());
        user.addPrivateAddress(privateAddress);
        userRepository.save(user);
        log.info("Private address added for user with pubId: {}", command.userPubId());
    }

    public void addCompanyAddress(AddCompanyAddressCommand command) {
        User user = UserUtil.findUserByPubId(userRepository, command.userPubId());
        CompanyAddress companyAddress = CompanyAddress.create(user.getId(), command.physicalAddress(), command.companyRecipient());
        user.addCompanyAddress(companyAddress);
        userRepository.save(user);
        log.info("Company address added for user with pubId: {}", command.userPubId());
    }

}
