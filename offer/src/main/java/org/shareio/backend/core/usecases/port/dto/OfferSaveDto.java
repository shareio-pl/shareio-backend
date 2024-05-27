package org.shareio.backend.core.usecases.port.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OfferSaveDto(
        UUID ownerId,
        LocalDateTime creationDate,
        String title,
        String condition,
        String category,
        String description,

        String country,
        String region,
        String city,
        String street,
        String houseNumber,
        String flatNumber,
        String postCode
) {

}
