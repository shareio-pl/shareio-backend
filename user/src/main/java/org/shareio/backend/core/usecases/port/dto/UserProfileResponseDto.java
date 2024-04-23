package org.shareio.backend.core.usecases.port.dto;


import org.shareio.backend.core.model.vo.AddressId;
import org.shareio.backend.core.model.vo.PhotoId;
import org.shareio.backend.core.model.vo.UserId;

import java.time.LocalDateTime;


public record UserProfileResponseDto(
        UserId userId,
        String email,
        String name,
        String surname,
        LocalDateTime dateOfBirth,
        PhotoId photoId,
        AddressId address,
        LocalDateTime lastLoginDate
) {
}
