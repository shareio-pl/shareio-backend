package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.OfferReviewDto;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.util.UUID;

public interface AddReviewUseCaseInterface {
    UUID addReview(OfferReviewDto offerReviewDto) throws MultipleValidationException;
}
