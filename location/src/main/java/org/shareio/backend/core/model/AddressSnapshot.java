package org.shareio.backend.core.model;

import org.shareio.backend.core.model.vo.AddressId;
import org.shareio.backend.core.model.vo.Location;

public record AddressSnapshot(AddressId addressId, String country, String region, String city, String houseNumber,
                              String flatNumber, String postCode, Location location) {
    public AddressSnapshot(Address address) {
        this(address.getAddressId(), address.getCountry(), address.getRegion(), address.getCity(), address.getHouseNumber(), address.getFlatNumber(), address.getPostCode(), address.getLocation());
    }
}
