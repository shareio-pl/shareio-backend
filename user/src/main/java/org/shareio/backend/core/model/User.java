package org.shareio.backend.core.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.shareio.backend.core.model.vo.Security;
import org.shareio.backend.core.model.vo.UserId;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    @Setter(AccessLevel.NONE)
    private UserId userId;
    private String email;
    private String name;
    private String surname;
    private LocalDateTime dateOfBirth;
    private Address address;
    private Security security;

    public static User fromDto(UserProfileGetDto userProfileGetDto) {
        return new User(
                userProfileGetDto.userId(),
                userProfileGetDto.email(),
                userProfileGetDto.name(),
                userProfileGetDto.surname(),
                userProfileGetDto.dateOfBirth(),
                new Address(userProfileGetDto.addressId(),
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
                        userProfileGetDto.lastLoginDate()
                )
        );
    }

    public UserSnapshot toSnapshot() {
        return new UserSnapshot(this);
    }
}
