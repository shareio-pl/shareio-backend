package org.shareio.backend.core.usecases.port.out;

import org.shareio.backend.core.usecases.port.dto.AddressGetDto;

import java.util.Optional;
import java.util.UUID;

public interface GetAddressDaoInterface {
    Optional<AddressGetDto> getAddressDto(UUID id);
}
