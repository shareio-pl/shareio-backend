package org.shareio.backend.core.usecases.port.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record OfferReviewDto
        (
                UUID offerId,
                Float reviewValue,
                LocalDateTime reviewDate
        ){ }
