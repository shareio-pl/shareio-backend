package org.shareio.backend.core.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.shareio.backend.Const;
import org.shareio.backend.core.model.vo.*;
import org.shareio.backend.core.usecases.port.dto.UserSaveDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class User {
    @Setter(AccessLevel.NONE)
    private UserId userId;
    private String email;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private PhotoId photoId;
    private Address address;
    private Security security;

    public static User fromDto(UserProfileGetDto userProfileGetDto) {
        return new User(
                new UserId(userProfileGetDto.userId()),
                userProfileGetDto.email(),
                userProfileGetDto.name(),
                userProfileGetDto.surname(),
                userProfileGetDto.dateOfBirth(),
                new PhotoId(userProfileGetDto.photoId()),

                new Address(new AddressId(userProfileGetDto.addressId()),
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ),
                new Security(
                        null,
                        null,
                        null,
                        userProfileGetDto.lastLoginDate()
                )
        );
    }

    public static User fromDto(UserSaveDto userSaveDto) {
        return new User(
                new UserId(UUID.randomUUID()),
                userSaveDto.email(),
                userSaveDto.name(),
                userSaveDto.surname(),
                userSaveDto.dateOfBirth(),
                new PhotoId(Const.defaultPhotoId),

                new Address(
                        new AddressId(UUID.randomUUID()),
                        userSaveDto.addressSaveDto().country(),
                        userSaveDto.addressSaveDto().region(),
                        userSaveDto.addressSaveDto().city(),
                        userSaveDto.addressSaveDto().street(),
                        userSaveDto.addressSaveDto().houseNumber(),
                        userSaveDto.addressSaveDto().flatNumber(),
                        userSaveDto.addressSaveDto().postCode(),
                        new Location(0.0, 0.0)
                ),
                new Security(
                        null,
                        AccountType.USER,
                        LocalDateTime.now(),
                        null
                )
        );
    }

    public UserSnapshot toSnapshot() {
        return new UserSnapshot(this);
    }
}
