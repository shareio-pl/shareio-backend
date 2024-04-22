package org.shareio.backend.core.usecases.port.dto;

import org.shareio.backend.core.model.vo.Condition;

import java.time.LocalDateTime;
import java.util.UUID;

public record OfferResponseDto(
        UUID offerId,
        UUID ownerId,
        UUID addressId,
        LocalDateTime creationDate,
        UUID receiverId,
        LocalDateTime reservationDate,

        String title,
        Condition condition,
        String description,
        String photoId
) {
}
