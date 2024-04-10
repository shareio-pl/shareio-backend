package org.shareio.backend.core.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.shareio.backend.core.model.vo.AddressId;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.usecases.port.dto.AddressGetDto;

@Getter
@Setter
@AllArgsConstructor
public class Address {
    @Setter(AccessLevel.NONE)
    private AddressId addressId;
    private String country;
    private String region;
    private String city;
    private String street;
    private String houseNumber;
    private String flatNumber;
    private String postCode;
    private Location location;

    public static Address fromDto(AddressGetDto addressGetDto) {
        return new Address(addressGetDto.addressId(),
                addressGetDto.country(),
                addressGetDto.region(),
                addressGetDto.city(),
                addressGetDto.street(),
                addressGetDto.houseNumber(),
                addressGetDto.flatNumber(),
                addressGetDto.postCode(),
                null);
    }

    public AddressSnapshot toSnapshot() {
        return new AddressSnapshot(this);
    }
}
