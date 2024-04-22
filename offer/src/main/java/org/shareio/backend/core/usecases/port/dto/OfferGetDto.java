package org.shareio.backend.core.usecases.port.dto;

import org.shareio.backend.core.model.vo.*;

import java.time.LocalDateTime;

public record OfferGetDto(
        OfferId offerId,
        UserId ownerId,
        AddressId addressId,
        LocalDateTime creationDate,
        UserId receiverId,
        LocalDateTime reservationDate,

        String title,
        Condition condition,
        String description,
        PhotoId photoId
) {
}
