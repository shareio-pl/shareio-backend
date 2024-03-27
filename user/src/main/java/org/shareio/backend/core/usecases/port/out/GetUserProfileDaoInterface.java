package org.shareio.backend.core.usecases.port.out;

import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;

import java.util.Optional;
import java.util.UUID;

public interface GetUserProfileDaoInterface {
    Optional<UserProfileGetDto> getUserDto(UUID id);
}
