package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.UserProfileResponseDto;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.util.UUID;

public interface GetUserProfileUseCaseInterface {
    UserProfileResponseDto getUserProfileResponseDto(UUID id) throws MultipleValidationException;
}
