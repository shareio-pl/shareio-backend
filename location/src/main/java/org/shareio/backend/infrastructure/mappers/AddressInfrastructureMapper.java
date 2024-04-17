package org.shareio.backend.infrastructure.mappers;

import org.shareio.backend.core.model.AddressSnapshot;
import org.shareio.backend.core.model.vo.LocationSnapshot;
import org.shareio.backend.core.usecases.port.dto.AddressResponseDto;
import org.shareio.backend.core.usecases.port.dto.LocationResponseDto;

public class AddressInfrastructureMapper {
    public static AddressResponseDto toDto(AddressSnapshot addressSnapshot) {
        return new AddressResponseDto(
                addressSnapshot.country(),
                addressSnapshot.region(),
                addressSnapshot.city(),
                addressSnapshot.street(),
                addressSnapshot.houseNumber(),
                addressSnapshot.flatNumber(),
                addressSnapshot.postCode()
        );
    }

    public static LocationResponseDto toDto(LocationSnapshot locationSnapshot) {
        return new LocationResponseDto(
                locationSnapshot.latitude(),
                locationSnapshot.longitude()
        );
    }
}
