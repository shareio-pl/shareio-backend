package org.shareio.backend.infrastructure.dbadapter.mappers;

import org.shareio.backend.core.usecases.port.dto.ReviewGetDto;
import org.shareio.backend.infrastructure.dbadapter.entities.ReviewEntity;

public class ReviewMapper {
    public static ReviewGetDto toDto(final ReviewEntity reviewEntity) {
        return new ReviewGetDto(
                reviewEntity.getReviewId(),
                OfferDatabaseMapper.toDto(reviewEntity.getOffer()),
                reviewEntity.getReviewValue(),
                reviewEntity.getReviewDate()
        );
    }
}
