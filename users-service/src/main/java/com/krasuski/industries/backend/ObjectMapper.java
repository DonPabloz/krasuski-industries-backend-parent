package com.krasuski.industries.backend;

import com.krasuski.industries.backend.dto.address.request.*;
import com.krasuski.industries.backend.dto.address.response.CompanyAddressResponse;
import com.krasuski.industries.backend.dto.address.response.LockerAddressResponse;
import com.krasuski.industries.backend.dto.address.response.PrivateAddressResponse;
import com.krasuski.industries.backend.dto.user.request.RegisterUserRequest;
import com.krasuski.industries.backend.dto.user.response.UserProfileInfoResponse;
import com.krasuski.industries.backend.entity.UserEntity;
import com.krasuski.industries.backend.entity.address.CompanyAddress;
import com.krasuski.industries.backend.entity.address.LockerAddress;
import com.krasuski.industries.backend.entity.address.PrivateAddress;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ObjectMapper {

    UserEntity destinationToSource(RegisterUserRequest destination);

    UserProfileInfoResponse userDetailsEntityToUserProfileInfoResponse(UserEntity userEntity);

    PrivateAddressResponse privateAddressEntityToDto(PrivateAddress privateAddress);

    PrivateAddress createPrivateAddressRequestToEntity(CreatePrivateAddressRequest request);

    PrivateAddress updatePrivateAddressRequestToEntity(UpdatePrivateAddressRequest request);

    CompanyAddressResponse companyAddressEntityToDto(CompanyAddress privateAddress);

    CompanyAddress createCompanyAddressRequestToEntity(CreateCompanyAddressRequest request);

    CompanyAddress updateCompanyAddressRequestToEntity(UpdateCompanyAddressRequest request);

    LockerAddressResponse lockerAddressEntityToDto(LockerAddress privateAddress);

    LockerAddress createLockerAddressRequestToEntity(CreateLockerAddressRequest request);

    LockerAddress updateLockerAddressRequestToEntity(UpdateLockerAddressRequest request);

    default String map(UUID value) {
        return value != null ? value.toString() : null;
    }
}
