package org.shareio.backend.infrastructure.dbadapter.mappers;

import org.shareio.backend.core.model.ReviewSnapshot;
import org.shareio.backend.infrastructure.dbadapter.entities.ReviewEntity;

public class ReviewMapper {

    public static ReviewEntity toEntity(final ReviewSnapshot reviewSnapshot) {
        return new ReviewEntity(
                null,
                reviewSnapshot.reviewId().getId(),
                reviewSnapshot.value(),
                reviewSnapshot.date()
        );
    }

    private ReviewMapper(){
        throw new IllegalStateException("Utility class");
    }
}
