package org.shareio.backend.core.usecases.port.dto;

import java.util.UUID;

public record OfferEndDto
        (
                UUID offerId,
                UUID userId
        ) {
}
