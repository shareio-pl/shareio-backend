package org.shareio.backend.infrastructure.dbadapter.mappers;

import org.shareio.backend.infrastructure.dbadapter.entities.ReviewEntity;

public class ReviewMapper {
    public static ReviewEntity toEntity(ReviewEntity reviewEntity) {
        return new ReviewEntity(null, reviewEntity.getReviewId(),
                reviewEntity.getOffer(), reviewEntity.getValue(),
                reviewEntity.getDate());
    }
}
