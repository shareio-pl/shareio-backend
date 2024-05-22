package org.shareio.backend.core.usecases.port.dto;


import org.shareio.backend.core.model.vo.AddressId;
import org.shareio.backend.core.model.vo.PhotoId;
import org.shareio.backend.core.model.vo.UserId;

import java.time.LocalDate;
import java.time.LocalDateTime;


public record UserProfileResponseDto( // TODO: flatten
        UserId userId,
        String email,
        String name,
        String surname,
        LocalDate dateOfBirth,
        PhotoId photoId,
        AddressId address,
        LocalDateTime lastLoginDate
) {
}
