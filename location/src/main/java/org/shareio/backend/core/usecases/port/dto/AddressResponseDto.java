package org.shareio.backend.core.usecases.port.dto;

import org.shareio.backend.core.model.vo.AddressId;
import org.shareio.backend.core.model.vo.Location;

public record AddressResponseDto(
        AddressId addressId,
        String country,
        String region,
        String city,
        String houseNumber,
        String flatNumber,
        String postCode
) {
}
