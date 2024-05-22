package org.shareio.backend.core.usecases.port.dto;

import java.util.UUID;

public record OfferReserveDto
        (
                UUID offerId,
                UUID recieverId
        ) {
}
