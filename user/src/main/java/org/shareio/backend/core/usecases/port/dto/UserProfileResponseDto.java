package org.shareio.backend.core.usecases.port.dto;


import java.time.LocalDateTime;
import java.util.UUID;


public record UserProfileResponseDto(
        UUID userId,
        String email,
        String name,
        LocalDateTime dateOfBirth,
        String country,
        String city,
        LocalDateTime lastLoginDate
) {
}
