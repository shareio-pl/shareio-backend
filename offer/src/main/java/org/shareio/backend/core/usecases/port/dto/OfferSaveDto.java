package org.shareio.backend.core.usecases.port.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OfferSaveDto(
        UUID ownerId,
        AddressSaveDto addressSaveDto,
        LocalDateTime creationDate,
        String title,
        String condition,
        String category,
        String description
) {

}
