package org.shareio.backend.core.usecases.port.dto;

import org.shareio.backend.core.model.vo.UserId;

import java.time.LocalDateTime;


public record UserProfileGetDto(
        UserId userId,
        String email,
        String name,
        LocalDateTime dateOfBirth,
        String country,
        String city,
        LocalDateTime lastLoginDate) {
}
