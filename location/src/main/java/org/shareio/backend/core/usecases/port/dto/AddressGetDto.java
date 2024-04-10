package org.shareio.backend.core.usecases.port.dto;

import org.shareio.backend.core.model.vo.AddressId;


public record AddressGetDto(
        AddressId addressId,
        String country,
        String region,
        String city,
        String street,
        String houseNumber,
        String flatNumber,
        String postCode
) {
}
