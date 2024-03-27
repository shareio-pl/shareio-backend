package org.shareio.backend.infrastructure.dbadapter.mappers;

import org.shareio.backend.core.model.UserSnapshot;
import org.shareio.backend.core.model.vo.UserId;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.infrastructure.dbadapter.entities.SecurityEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;

public class UserMapper {

    public static UserProfileGetDto toDto(UserEntity userEntity) {
        return new UserProfileGetDto(
                new UserId(userEntity.getUserId()),
                userEntity.getEmail(),
                userEntity.getName(),
                userEntity.getDateOfBirth(),
                userEntity.getAddress().getCountry(),
                userEntity.getAddress().getCity(),
                userEntity.getSecurity().getLastLoginDate()
        );
    }

    public static UserEntity toEntity(UserSnapshot userSnapshot) {
        return new UserEntity(null, userSnapshot.userId().getId(), userSnapshot.email(),
                userSnapshot.name(), userSnapshot.dateOfBirth(),
                AddressMapper.map(userSnapshot.address()), UserMapper.toSecurityEntity(userSnapshot));
    }

    private static SecurityEntity toSecurityEntity(UserSnapshot userSnapshot) {
        return new SecurityEntity(
                null,
                userSnapshot.security().getPwHash(),
                userSnapshot.security().getRegistrationDate(),
                userSnapshot.security().getLastLoginDate());
    }
}
