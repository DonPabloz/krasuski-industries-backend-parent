package com.krasuski.industries.backend.service;

import com.krasuski.industries.backend.AbstractUnitTest;
import com.krasuski.industries.backend.ObjectMapper;
import com.krasuski.industries.backend.ObjectMapperImpl;
import com.krasuski.industries.backend.dto.address.request.*;
import com.krasuski.industries.backend.dto.user.response.ReceiversResponse;
import com.krasuski.industries.backend.exception.AddressNotFoundException;
import com.krasuski.industries.backend.exception.user.UserNotFoundException;
import com.krasuski.industries.backend.repositories.address.CompanyAddressRepository;
import com.krasuski.industries.backend.repositories.address.LockerAddressRepository;
import com.krasuski.industries.backend.repositories.address.PrivateAddressRepository;
import com.krasuski.industries.backend.repositories.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PrivateAddressServiceTest extends AbstractUnitTest {

    private final ObjectMapper objectMapper = new ObjectMapperImpl();
    @Mock
    private CompanyAddressRepository companyAddressRepository;
    @Mock
    private PrivateAddressRepository privateAddressRepository;
    @Mock
    private LockerAddressRepository lockerAddressRepository;
    @Mock
    private UserRepository userRepository;
    private AddressService addressService;

    @BeforeEach
    void setUp() {
        addressService = new AddressService(companyAddressRepository, privateAddressRepository, lockerAddressRepository, objectMapper, userRepository);
    }

    @Test
    void givenNullUserId_whenGettingAddresses_thenThrowException() {
        // given
        Long userId = null;

        // when

        // then
        assertThrows(UserNotFoundException.class, () -> addressService.getAddresses(userId));
    }

    @Test
    void givenNegativeUserId_whenGettingAddresses_thenThrowException() {
        // given
        Long userId = -1L;

        // when

        // then
        assertThrows(UserNotFoundException.class, () -> addressService.getAddresses(userId));
    }

    @Test
    void givenUserId_whenGettingAddressesForUserWithNoSavedAddresses_thenReturnEmptyResponse() {
        // given
        Long userId = 1L;

        // when
        when(privateAddressRepository.findAllByUserId(userId)).thenReturn(new ArrayList<>());
        when(companyAddressRepository.findAllByUserId(userId)).thenReturn(new ArrayList<>());
        when(lockerAddressRepository.findAllByUserId(userId)).thenReturn(new ArrayList<>());

        ReceiversResponse addresses = addressService.getAddresses(userId);

        // then
        assertNotNull(addresses);
        assertEquals(0, addresses.getPrivateReceivers().size());
        assertEquals(0, addresses.getCompanyReceivers().size());
        assertEquals(0, addresses.getLockerAddressResponseList().size());
    }

    @Test
    void givenUserId_whenGettingAddressesForUserWithSavedAddresses_thenReturnAddresses() {
        // given
        Long userId = 1L;

        // when
        when(privateAddressRepository.findAllByUserId(userId)).thenReturn(List.of(getPrivateAddress()));
        when(companyAddressRepository.findAllByUserId(userId)).thenReturn(List.of(getCompanyAddress()));
        when(lockerAddressRepository.findAllByUserId(userId)).thenReturn(List.of(getLockerAddress()));

        ReceiversResponse addresses = addressService.getAddresses(userId);

        // then
        assertNotNull(addresses);
        assertEquals(1, addresses.getPrivateReceivers().size());
        assertEquals(1, addresses.getCompanyReceivers().size());
        assertEquals(1, addresses.getLockerAddressResponseList().size());
    }

    @Test
    void givenNotExistingUserId_whenCreatingPrivateAddress_thenThrowException() {
        // given
        Long userId = 1L;
        CreatePrivateAddressRequest createPrivateAddressRequest = new CreatePrivateAddressRequest();

        // when
        when(userRepository.getReferenceById(userId)).thenThrow(EntityNotFoundException.class);

        // then
        assertThrows(EntityNotFoundException.class, () -> addressService.createPrivateAddress(userId, createPrivateAddressRequest));
    }

    @Test
    void givenNotExistingPrivateAddress_whenUpdatingPrivateAddress_thenThrowException() {
        // given
        Long userId = 1L;
        UpdatePrivateAddressRequest updatePrivateAddressRequest = new UpdatePrivateAddressRequest();

        // when
        when(privateAddressRepository.findByPublicId(updatePrivateAddressRequest.getPublicId())).thenReturn(Optional.empty());

        // then
        assertThrows(AddressNotFoundException.class, () -> addressService.updatePrivateAddress(userId, updatePrivateAddressRequest));
    }

    @Test
    void givenUserIsNotOwnerOfPrivateAddress_whenUpdatingPrivateAddress_thenThrowException() {
        // given
        Long userId = userWithoutAddresses.getId();
        UpdatePrivateAddressRequest updatePrivateAddressRequest = new UpdatePrivateAddressRequest();
        updatePrivateAddressRequest.setPublicId(getPrivateAddress().getPublicId());

        // when
        when(privateAddressRepository.findByPublicId(updatePrivateAddressRequest.getPublicId())).thenReturn(Optional.of(getPrivateAddress()));

        // then
        assertThrows(AddressNotFoundException.class, () -> addressService.updatePrivateAddress(userId, updatePrivateAddressRequest));
    }

    @Test
    void givenNotExistingPrivateAddress_whenDeletingPrivateAddress_thenThrowException() {
        // given
        Long userId = 1L;
        UUID privateAddressPublicId = getPrivateAddress().getPublicId();

        // when
        when(privateAddressRepository.findByPublicId(privateAddressPublicId)).thenReturn(Optional.empty());

        // then
        assertThrows(AddressNotFoundException.class, () -> addressService.deletePrivateAddress(userId, privateAddressPublicId));
    }


    @Test
    void givenUserIsNotOwnerOfPrivateAddress_whenDeletingPrivateAddress_thenThrowException() {
        // given
        Long userId = userWithoutAddresses.getId();
        UUID privateAddressPublicId = getPrivateAddress().getPublicId();

        // when
        when(privateAddressRepository.findByPublicId(privateAddressPublicId)).thenReturn(Optional.of(getPrivateAddress()));

        // then
        assertThrows(AddressNotFoundException.class, () -> addressService.deletePrivateAddress(userId, privateAddressPublicId));
    }

    @Test
    void givenNotExistingUserId_whenCreatingCompanyAddress_thenThrowException() {
        // given
        Long userId = 1L;
        CreateCompanyAddressRequest createCompanyAddressRequest = new CreateCompanyAddressRequest();

        // when
        when(userRepository.getReferenceById(userId)).thenThrow(EntityNotFoundException.class);

        // then
        assertThrows(EntityNotFoundException.class, () -> addressService.createCompanyAddress(userId, createCompanyAddressRequest));
    }

    @Test
    void givenNotExistingCompanyAddress_whenUpdatingCompanyAddress_thenThrowException() {
        // given
        Long userId = 1L;
        UpdateCompanyAddressRequest updateCompanyAddressRequest = new UpdateCompanyAddressRequest();

        // when
        when(companyAddressRepository.findByPublicId(updateCompanyAddressRequest.getPublicId())).thenReturn(Optional.empty());

        // then
        assertThrows(AddressNotFoundException.class, () -> addressService.updateCompanyAddress(userId, updateCompanyAddressRequest));
    }

    @Test
    void givenUserIsNotOwnerOfCompanyAddress_whenUpdatingCompanyAddress_thenThrowException() {
        // given
        Long userId = userWithoutAddresses.getId();
        UpdateCompanyAddressRequest updateCompanyAddressRequest = new UpdateCompanyAddressRequest();
        updateCompanyAddressRequest.setPublicId(getCompanyAddress().getPublicId());

        // when
        when(companyAddressRepository.findByPublicId(updateCompanyAddressRequest.getPublicId())).thenReturn(Optional.of(getCompanyAddress()));

        // then
        assertThrows(AddressNotFoundException.class, () -> addressService.updateCompanyAddress(userId, updateCompanyAddressRequest));
    }

    @Test
    void givenNotExistingCompanyAddress_whenDeletingCompanyAddress_thenThrowException() {
        // given
        Long userId = 1L;
        UUID companyAddressPublicId = getCompanyAddress().getPublicId();

        // when
        when(companyAddressRepository.findByPublicId(companyAddressPublicId)).thenReturn(Optional.empty());

        // then
        assertThrows(AddressNotFoundException.class, () -> addressService.deleteCompanyAddress(userId, companyAddressPublicId));
    }


    @Test
    void givenUserIsNotOwnerOfCompanyAddress_whenDeletingCompanyAddress_thenThrowException() {
        // given
        Long userId = userWithoutAddresses.getId();
        UUID companyAddressPublicId = getCompanyAddress().getPublicId();

        // when
        when(companyAddressRepository.findByPublicId(companyAddressPublicId)).thenReturn(Optional.of(getCompanyAddress()));

        // then
        assertThrows(AddressNotFoundException.class, () -> addressService.deleteCompanyAddress(userId, companyAddressPublicId));
    }

    @Test
    void givenNotExistingUserId_whenCreatingLockerAddress_thenThrowException() {
        // given
        Long userId = 1L;
        CreateLockerAddressRequest createLockerAddressRequest = new CreateLockerAddressRequest();

        // when
        when(userRepository.getReferenceById(userId)).thenThrow(EntityNotFoundException.class);

        // then
        assertThrows(EntityNotFoundException.class, () -> addressService.createLockerAddress(userId, createLockerAddressRequest));
    }

    @Test
    void givenNotExistingLockerAddress_whenUpdatingLockerAddress_thenThrowException() {
        // given
        Long userId = 1L;
        UpdateLockerAddressRequest updateLockerAddressRequest = new UpdateLockerAddressRequest();

        // when
        when(lockerAddressRepository.findByPublicId(updateLockerAddressRequest.getPublicId())).thenReturn(Optional.empty());

        // then
        assertThrows(AddressNotFoundException.class, () -> addressService.updateLockerAddress(userId, updateLockerAddressRequest));
    }

    @Test
    void givenUserIsNotOwnerOfLockerAddress_whenUpdatingLockerAddress_thenThrowException() {
        // given
        Long userId = userWithoutAddresses.getId();
        UpdateLockerAddressRequest updateLockerAddressRequest = new UpdateLockerAddressRequest();
        updateLockerAddressRequest.setPublicId(getLockerAddress().getPublicId());

        // when
        when(lockerAddressRepository.findByPublicId(updateLockerAddressRequest.getPublicId())).thenReturn(Optional.of(getLockerAddress()));

        // then
        assertThrows(AddressNotFoundException.class, () -> addressService.updateLockerAddress(userId, updateLockerAddressRequest));
    }

    @Test
    void givenNotExistingLockerAddress_whenDeletingLockerAddress_thenThrowException() {
        // given
        Long userId = 1L;
        UUID lockerAddressPublicId = getLockerAddress().getPublicId();

        // when
        when(lockerAddressRepository.findByPublicId(lockerAddressPublicId)).thenReturn(Optional.empty());

        // then
        assertThrows(AddressNotFoundException.class, () -> addressService.deleteLockerAddress(userId, lockerAddressPublicId));
    }


    @Test
    void givenUserIsNotOwnerOfLockerAddress_whenDeletingLockerAddress_thenThrowException() {
        // given
        Long userId = userWithoutAddresses.getId();
        UUID lockerAddressPublicId = getLockerAddress().getPublicId();

        // when
        when(lockerAddressRepository.findByPublicId(lockerAddressPublicId)).thenReturn(Optional.of(getLockerAddress()));

        // then
        assertThrows(AddressNotFoundException.class, () -> addressService.deleteLockerAddress(userId, lockerAddressPublicId));
    }
}