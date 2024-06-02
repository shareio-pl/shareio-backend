package org.shareio.backend.core.usecases.port.dto;

import java.util.UUID;

public record UserScoreWithPositionDto(
        UUID userId,
        String nameAndSurname,
        Double score,
        Integer position
) {
}
