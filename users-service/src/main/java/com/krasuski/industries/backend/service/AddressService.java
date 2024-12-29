package com.krasuski.industries.backend.service;

import com.krasuski.industries.backend.ObjectMapper;
import com.krasuski.industries.backend.dto.address.request.*;
import com.krasuski.industries.backend.dto.address.response.CompanyAddressResponse;
import com.krasuski.industries.backend.dto.address.response.LockerAddressResponse;
import com.krasuski.industries.backend.dto.address.response.PrivateAddressResponse;
import com.krasuski.industries.backend.dto.user.response.ReceiversResponse;
import com.krasuski.industries.backend.entity.address.CompanyAddress;
import com.krasuski.industries.backend.entity.address.LockerAddress;
import com.krasuski.industries.backend.entity.address.PrivateAddress;
import com.krasuski.industries.backend.exception.AddressNotFoundException;
import com.krasuski.industries.backend.exception.user.UserNotFoundException;
import com.krasuski.industries.backend.repositories.address.CompanyAddressRepository;
import com.krasuski.industries.backend.repositories.address.LockerAddressRepository;
import com.krasuski.industries.backend.repositories.address.PrivateAddressRepository;
import com.krasuski.industries.backend.repositories.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AddressService {

    private final CompanyAddressRepository companyAddressRepository;
    private final PrivateAddressRepository privateAddressRepository;
    private final LockerAddressRepository lockerAddressRepository;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    public AddressService(CompanyAddressRepository companyAddressRepository, PrivateAddressRepository privateAddressRepository, LockerAddressRepository lockerAddressRepository, ObjectMapper objectMapper, UserRepository userRepository) {
        this.companyAddressRepository = companyAddressRepository;
        this.privateAddressRepository = privateAddressRepository;
        this.lockerAddressRepository = lockerAddressRepository;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    public ReceiversResponse getAddresses(Long userId) {
        if (userId == null || userId < 0) {
            log.error("User with id {} not found", userId);
            throw new UserNotFoundException();
        }
        List<PrivateAddressResponse> privateAddressResponseList = getPrivateAddresses(userId);
        List<CompanyAddressResponse> companyAddressDtoList = getCompanyAddresses(userId);
        List<LockerAddressResponse> lockerAddressResponseList = getLockerAddresses(userId);

        return new ReceiversResponse(privateAddressResponseList, companyAddressDtoList, lockerAddressResponseList);
    }

    public PrivateAddressResponse createPrivateAddress(Long userId, CreatePrivateAddressRequest createPrivateAddressRequest) {
        PrivateAddress privateAddress = objectMapper.createPrivateAddressRequestToEntity(createPrivateAddressRequest);
        privateAddress.setUser(userRepository.getReferenceById(userId));
        PrivateAddress savedPrivateAddress = privateAddressRepository.save(privateAddress);

        return objectMapper.privateAddressEntityToDto(savedPrivateAddress);
    }

    public PrivateAddressResponse updatePrivateAddress(Long userId, UpdatePrivateAddressRequest updatePrivateAddressRequest) {
        PrivateAddress privateAddressFromDb = privateAddressRepository.findByPublicId(updatePrivateAddressRequest.getPublicId())
                .orElseThrow(() -> {
                    log.error("Private address with publicId {} not found", updatePrivateAddressRequest.getPublicId());
                    throw new AddressNotFoundException(updatePrivateAddressRequest.getPublicId());
                });

        if (!privateAddressFromDb.getUser().getId().equals(userId)) {
            log.error("User with id {} is not owner of private address with publicId {}", userId, updatePrivateAddressRequest.getPublicId());
            throw new AddressNotFoundException(updatePrivateAddressRequest.getPublicId());
        }

        PrivateAddress privateAddress = objectMapper.updatePrivateAddressRequestToEntity(updatePrivateAddressRequest);
        privateAddress.setId(privateAddressFromDb.getId());
        PrivateAddress savedPrivateAddress = privateAddressRepository.save(privateAddress);

        return objectMapper.privateAddressEntityToDto(savedPrivateAddress);
    }

    public UUID deletePrivateAddress(Long userId, UUID publicId) {
        PrivateAddress privateAddressFromDb = privateAddressRepository.findByPublicId(publicId)
                .orElseThrow(() -> {
                    log.error("Private address with publicId {} not found", publicId);
                    throw new AddressNotFoundException(publicId);
                });

        if (!privateAddressFromDb.getUser().getId().equals(userId)) {
            log.error("User with id {} is not owner of private address with publicId {}", userId, publicId);
            throw new AddressNotFoundException(publicId);
        }

        privateAddressRepository.delete(privateAddressFromDb);

        return publicId;
    }

    public CompanyAddressResponse createCompanyAddress(Long userId, CreateCompanyAddressRequest createCompanyAddressRequest) {
        CompanyAddress companyAddress = objectMapper.createCompanyAddressRequestToEntity(createCompanyAddressRequest);
        companyAddress.setUser(userRepository.getReferenceById(userId));
        CompanyAddress savedCompanyAddress = companyAddressRepository.save(companyAddress);

        return objectMapper.companyAddressEntityToDto(savedCompanyAddress);
    }

    public CompanyAddressResponse updateCompanyAddress(Long userId, UpdateCompanyAddressRequest updateCompanyAddressRequest) {
        CompanyAddress companyAddressFromDb = companyAddressRepository.findByPublicId(updateCompanyAddressRequest.getPublicId())
                .orElseThrow(() -> {
                    log.error("Company address with publicId {} not found", updateCompanyAddressRequest.getPublicId());
                    throw new AddressNotFoundException(updateCompanyAddressRequest.getPublicId());
                });

        if (!companyAddressFromDb.getUser().getId().equals(userId)) {
            log.error("User with id {} is not owner of company address with publicId {}", userId, updateCompanyAddressRequest.getPublicId());
            throw new AddressNotFoundException(updateCompanyAddressRequest.getPublicId());
        }

        CompanyAddress companyAddress = objectMapper.updateCompanyAddressRequestToEntity(updateCompanyAddressRequest);
        companyAddress.setId(companyAddressFromDb.getId());
        CompanyAddress savedCompanyAddress = companyAddressRepository.save(companyAddress);

        return objectMapper.companyAddressEntityToDto(savedCompanyAddress);
    }

    public UUID deleteCompanyAddress(Long userId, UUID publicId) {
        CompanyAddress companyAddressFromDb = companyAddressRepository.findByPublicId(publicId)
                .orElseThrow(() -> {
                    log.error("Company address with publicId {} not found", publicId);
                    throw new AddressNotFoundException(publicId);
                });

        if (!companyAddressFromDb.getUser().getId().equals(userId)) {
            log.error("User with id {} is not owner of company address with publicId {}", userId, publicId);
            throw new AddressNotFoundException(publicId);
        }

        companyAddressRepository.delete(companyAddressFromDb);

        return publicId;
    }

    public LockerAddressResponse createLockerAddress(Long userId, CreateLockerAddressRequest createLockerAddressRequest) {
        LockerAddress lockerAddress = objectMapper.createLockerAddressRequestToEntity(createLockerAddressRequest);
        lockerAddress.setUser(userRepository.getReferenceById(userId));
        LockerAddress savedLockerAddress = lockerAddressRepository.save(lockerAddress);

        return objectMapper.lockerAddressEntityToDto(savedLockerAddress);
    }

    public LockerAddressResponse updateLockerAddress(Long userId, UpdateLockerAddressRequest updateLockerAddressRequest) {
        LockerAddress lockerAddressFromDb = lockerAddressRepository.findByPublicId(updateLockerAddressRequest.getPublicId())
                .orElseThrow(() -> {
                    log.error("Locker address with publicId {} not found", updateLockerAddressRequest.getPublicId());
                    throw new AddressNotFoundException(updateLockerAddressRequest.getPublicId());
                });

        if (!lockerAddressFromDb.getUser().getId().equals(userId)) {
            log.error("User with id {} is not owner of locker address with publicId {}", userId, updateLockerAddressRequest.getPublicId());
            throw new AddressNotFoundException(updateLockerAddressRequest.getPublicId());
        }

        LockerAddress lockerAddress = objectMapper.updateLockerAddressRequestToEntity(updateLockerAddressRequest);
        lockerAddress.setId(lockerAddressFromDb.getId());
        LockerAddress savedLockerAddress = lockerAddressRepository.save(lockerAddress);

        return objectMapper.lockerAddressEntityToDto(savedLockerAddress);
    }

    public UUID deleteLockerAddress(Long userId, UUID publicId) {
        LockerAddress lockerAddressFromDb = lockerAddressRepository.findByPublicId(publicId)
                .orElseThrow(() -> {
                    log.error("Locker address with publicId {} not found", publicId);
                    throw new AddressNotFoundException(publicId);
                });

        if (!lockerAddressFromDb.getUser().getId().equals(userId)) {
            log.error("User with id {} is not owner of locker address with publicId {}", userId, publicId);
            throw new AddressNotFoundException(publicId);
        }

        lockerAddressRepository.delete(lockerAddressFromDb);

        return publicId;
    }

    private List<PrivateAddressResponse> getPrivateAddresses(Long userId) {
        List<PrivateAddress> allByUserId = privateAddressRepository.findAllByUserId(userId);

        return allByUserId.stream().map(objectMapper::privateAddressEntityToDto).toList();
    }

    private List<CompanyAddressResponse> getCompanyAddresses(Long userId) {
        List<CompanyAddress> allByUserId = companyAddressRepository.findAllByUserId(userId);

        return allByUserId.stream().map(objectMapper::companyAddressEntityToDto).toList();
    }

    private List<LockerAddressResponse> getLockerAddresses(Long userId) {
        List<LockerAddress> allByUserId = lockerAddressRepository.findAllByUserId(userId);

        return allByUserId.stream().map(objectMapper::lockerAddressEntityToDto).toList();
    }
}
