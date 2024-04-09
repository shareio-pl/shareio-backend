package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.AddressResponseDto;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.util.UUID;

public interface GetAddressUseCaseInterface {
    AddressResponseDto getAddressResponseDto(UUID id) throws MultipleValidationException;
}
