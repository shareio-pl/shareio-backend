package org.shareio.backend.infrastructure.dbadapter.mappers;

import org.shareio.backend.core.model.Address;
import org.shareio.backend.infrastructure.dbadapter.entities.AddressEntity;

public class AddressMapper {
    public static AddressEntity map(final Address address) {
        return new AddressEntity(
                null,
                address.getAddressId().getId(),
                address.getCountry(),
                address.getRegion(),
                address.getCity(),
                address.getHouseNumber(),
                address.getFlatNumber(),
                address.getPostCode(),
                address.getLocation().getLatitude(),
                address.getLocation().getLongitude());
    }
}
