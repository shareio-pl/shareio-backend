package org.shareio.backend.infrastructure.mappers;

import org.shareio.backend.core.model.AddressSnapshot;
import org.shareio.backend.core.usecases.port.dto.AddressResponseDto;

public class AddressInfrastructureMapper {
    public static AddressResponseDto toDto(AddressSnapshot addressSnapshot) {
        return new AddressResponseDto(
                addressSnapshot.addressId(),
                addressSnapshot.country(),
                addressSnapshot.region(),
                addressSnapshot.city(),
                addressSnapshot.houseNumber(),
                addressSnapshot.flatNumber(),
                addressSnapshot.postCode()
        );
    }
}
