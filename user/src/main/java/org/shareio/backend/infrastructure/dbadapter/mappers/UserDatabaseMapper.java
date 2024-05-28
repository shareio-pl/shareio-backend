package org.shareio.backend.infrastructure.dbadapter.mappers;

import org.shareio.backend.core.model.UserSnapshot;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.infrastructure.dbadapter.entities.SecurityEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;

public class UserDatabaseMapper {

    public static UserProfileGetDto toDto(final UserEntity userEntity) {
        return new UserProfileGetDto(
                userEntity.getUserId(),
                userEntity.getEmail(),
                userEntity.getName(),
                userEntity.getSurname(),
                userEntity.getDateOfBirth(),
                userEntity.getPhotoId(),
                userEntity.getAddress().getAddressId(),
                userEntity.getSecurity().getLastLoginDate(),
                userEntity.getSecurity().getPwHash()
        );
    }

    public static UserEntity toEntity(final UserSnapshot userSnapshot) {
        return new UserEntity(null, userSnapshot.userId().getId(), userSnapshot.email(),
                userSnapshot.name(), userSnapshot.surname(), userSnapshot.dateOfBirth(), userSnapshot.photoId().getId(),
                AddressDatabaseMapper.toEntity(userSnapshot.address()), UserDatabaseMapper.toSecurityEntity(userSnapshot));
    }

    private static SecurityEntity toSecurityEntity(final UserSnapshot userSnapshot) {
        return new SecurityEntity(
                null,
                userSnapshot.security().getPwHash(),
                userSnapshot.security().getAccountType(),
                userSnapshot.security().getRegistrationDate(),
                userSnapshot.security().getLastLoginDate());
    }

    private UserDatabaseMapper() {
        throw new IllegalArgumentException("Utility class");
    }
}
