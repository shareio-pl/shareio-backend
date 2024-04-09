package org.shareio.backend.core.usecases.port.out;

import org.shareio.backend.core.usecases.port.dto.LocationGetDto;

import java.util.Optional;
import java.util.UUID;

public interface GetLocationDaoInterface {
    Optional<LocationGetDto> getLocationDto(UUID id);
}
