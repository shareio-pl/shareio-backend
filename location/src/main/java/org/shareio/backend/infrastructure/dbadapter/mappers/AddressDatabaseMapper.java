package org.shareio.backend.infrastructure.dbadapter.mappers;

import org.shareio.backend.core.model.Address;
import org.shareio.backend.core.model.vo.AddressId;
import org.shareio.backend.core.usecases.port.dto.AddressGetDto;
import org.shareio.backend.core.usecases.port.dto.LocationGetDto;
import org.shareio.backend.infrastructure.dbadapter.entities.AddressEntity;

public class AddressDatabaseMapper {
    public static AddressGetDto toDto(final AddressEntity address) {
        return new AddressGetDto(
                new AddressId(address.getAddressId()),
                address.getCountry(),
                address.getRegion(),
                address.getCity(),
                address.getStreet(),
                address.getHouseNumber(),
                address.getFlatNumber(),
                address.getPostCode());
    }

    public static LocationGetDto toLocationDto(final AddressEntity address) {
        return new LocationGetDto(
                address.getLatitude(),
                address.getLongitude());
    }

    public static AddressEntity toEntity(final Address address) {
        return new AddressEntity(
                null,
                address.getAddressId().getId(),
                address.getCountry(),
                address.getRegion(),
                address.getCity(),
                address.getStreet(),
                address.getHouseNumber(),
                address.getFlatNumber(),
                address.getPostCode(),
                address.getLocation().getLatitude(),
                address.getLocation().getLongitude());
    }
}
