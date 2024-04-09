package org.shareio.backend.core.usecases.port.dto;


import org.shareio.backend.core.model.vo.AddressId;
import org.shareio.backend.core.model.vo.UserId;

import java.time.LocalDateTime;
import java.util.UUID;


public record UserProfileResponseDto(
        UserId userId,
        String email,
        String name,
        LocalDateTime dateOfBirth,
        AddressId address,
        LocalDateTime lastLoginDate
) {
}
