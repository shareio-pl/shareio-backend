package org.shareio.backend.core.usecases.port.dto;

import java.util.UUID;


public record AddressGetDto(
        UUID addressId,
        String country,
        String region,
        String city,
        String street,
        String houseNumber,
        String flatNumber,
        String postCode
) {
}
