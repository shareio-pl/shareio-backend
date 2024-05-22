package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.OfferReviewDto;

import java.util.UUID;

public interface AddReviewUseCaseInterface {
    UUID addReview(OfferReviewDto offerReviewDto);
}
