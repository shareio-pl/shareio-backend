package org.shareio.backend.core.usecases.port.dto;

import java.util.UUID;

public record OfferReviewDto
        (
                UUID offerId,
                Double reviewValue
        ){ }
