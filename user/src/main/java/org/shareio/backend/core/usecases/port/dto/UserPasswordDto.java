package org.shareio.backend.core.usecases.port.dto;

public record UserPasswordDto(
        String oldPassword,
        String newPassword
) {
}
