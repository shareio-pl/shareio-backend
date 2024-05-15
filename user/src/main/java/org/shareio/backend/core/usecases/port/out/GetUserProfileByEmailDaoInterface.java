package org.shareio.backend.core.usecases.port.out;

import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;

import java.util.Optional;

public interface GetUserProfileByEmailDaoInterface {
    Optional<UserProfileGetDto> getUserDto(String email);
}
