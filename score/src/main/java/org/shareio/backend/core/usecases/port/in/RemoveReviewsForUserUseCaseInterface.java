package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.RemoveResponseDto;

import java.util.UUID;

public interface RemoveReviewsForUserUseCaseInterface {
    RemoveResponseDto removeReviewsForUser(UUID userId, RemoveResponseDto removeResponseDto);
}
