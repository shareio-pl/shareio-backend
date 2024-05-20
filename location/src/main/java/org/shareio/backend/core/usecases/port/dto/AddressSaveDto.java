package org.shareio.backend.core.usecases.port.dto;

public record AddressSaveDto(
        String country,
        String region,
        String city,
        String street,
        String houseNumber,
        String flatNumber,
        String postCode
) {
}
