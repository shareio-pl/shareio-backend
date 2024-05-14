package org.shareio.backend.core.usecases.port.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OfferGetDto(
        UUID offerId,
        LocalDateTime creationDate,
        String status,

        String city,
        String street,
        String houseNumber,
        //String distance,
        Double latitude,
        Double longitude,

        String title,
        String condition,
        String category,
        String description,
        UUID photoId,

        UUID ownerId,
        String ownerName,
        String ownerSurname,
        UUID ownerPhotoId,
        Double ownerRating,
        Integer ownerReviewCount,

        LocalDateTime reservationDate
) {
}
