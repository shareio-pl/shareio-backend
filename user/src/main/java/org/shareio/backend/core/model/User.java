package org.shareio.backend.core.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.shareio.backend.Const;
import org.shareio.backend.core.model.vo.*;
import org.shareio.backend.core.usecases.port.dto.UserAddDto;
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

    public static User fromDto(UserAddDto userAddDto) {
        return new User(
                new UserId(UUID.randomUUID()),
                userAddDto.email(),
                userAddDto.name(),
                userAddDto.surname(),
                userAddDto.dateOfBirth(),
                new PhotoId(Const.defaultPhotoId),

                new Address(
                        new AddressId(UUID.randomUUID()),
                        userAddDto.country(),
                        userAddDto.region(),
                        userAddDto.city(),
                        userAddDto.street(),
                        userAddDto.houseNumber(),
                        userAddDto.flatNumber(),
                        userAddDto.postcode(),
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
