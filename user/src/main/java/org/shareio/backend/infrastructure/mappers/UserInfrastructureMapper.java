package org.shareio.backend.infrastructure.mappers;

import org.shareio.backend.core.model.UserSnapshot;
import org.shareio.backend.core.usecases.port.dto.UserProfileResponseDto;

public class UserInfrastructureMapper {

    public static UserProfileResponseDto toDto(UserSnapshot userSnapshot) {
        return new UserProfileResponseDto(
                userSnapshot.userId(),
                userSnapshot.email(),
                userSnapshot.name(),
                userSnapshot.surname(),
                userSnapshot.dateOfBirth(),
                userSnapshot.address().getAddressId(),
                userSnapshot.security().getLastLoginDate()
        );
    }
}
