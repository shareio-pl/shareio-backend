package org.shareio.backend.core.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.shareio.backend.core.model.vo.AddressId;
import org.shareio.backend.core.model.vo.Location;

@Getter
@Setter
@AllArgsConstructor
public class Address {
    @Setter(AccessLevel.NONE)
    private AddressId addressId;
    private String country;
    private String region;
    private String city;
    private String houseNumber;
    private String flatNumber;
    private String postCode;
    private Location location;
}
