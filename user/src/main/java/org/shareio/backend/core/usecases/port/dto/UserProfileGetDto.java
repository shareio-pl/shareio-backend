package org.shareio.backend.core.usecases.port.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


public record UserProfileGetDto(
        UUID userId,
        String email,
        String name,
        String surname,
        LocalDate dateOfBirth,
        UUID photoId,
        UUID addressId,
        LocalDateTime lastLoginDate,
        String pwHash) {
}
