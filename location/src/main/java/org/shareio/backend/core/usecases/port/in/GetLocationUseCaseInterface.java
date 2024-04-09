package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.LocationResponseDto;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.util.UUID;

public interface GetLocationUseCaseInterface {
    LocationResponseDto getLocationResponseDto(UUID id) throws MultipleValidationException;
}
