package com.krasuski.industries.backend.dto.user.response;

import com.krasuski.industries.backend.dto.address.response.CompanyAddressResponse;
import com.krasuski.industries.backend.dto.address.response.PrivateAddressResponse;
import lombok.Value;

import java.util.List;

@Value
public class BuyersResponse {
    List<PrivateAddressResponse> privateBuyers;
    List<CompanyAddressResponse> companyBuyers;
}
