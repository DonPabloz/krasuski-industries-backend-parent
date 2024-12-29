package com.krasuski.industries.backend.dto.user.response;

import com.krasuski.industries.backend.dto.address.response.CompanyAddressResponse;
import com.krasuski.industries.backend.dto.address.response.LockerAddressResponse;
import com.krasuski.industries.backend.dto.address.response.PrivateAddressResponse;
import lombok.Value;

import java.util.List;

@Value
public class ReceiversResponse {
    List<PrivateAddressResponse> privateReceivers;
    List<CompanyAddressResponse> companyReceivers;
    List<LockerAddressResponse> lockerAddressResponseList;
}
