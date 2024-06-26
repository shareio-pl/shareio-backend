package org.shareio.backend.core.usecases.port.dto;

import java.util.UUID;

public record UserScoreDto(
        UUID userId,
        String nameAndSurname,
        Double score
) {
}
