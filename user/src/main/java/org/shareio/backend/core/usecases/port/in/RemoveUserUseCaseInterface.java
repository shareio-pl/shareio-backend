package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.RemoveResponseDto;

import java.util.UUID;

public interface RemoveUserUseCaseInterface {
    RemoveResponseDto removeUser(UUID userId, RemoveResponseDto removeResponseDto);
}
