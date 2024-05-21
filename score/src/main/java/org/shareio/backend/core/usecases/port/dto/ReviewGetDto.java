package org.shareio.backend.core.usecases.port.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReviewGetDto(
        UUID reviewId,
        Float value,
        LocalDateTime date
)
{

}
