package org.shareio.backend.core.model;

import org.shareio.backend.core.model.vo.ReviewId;

import java.time.LocalDateTime;

public record ReviewSnapshot(ReviewId reviewId, Offer offer, Integer value, LocalDateTime date) {
    public ReviewSnapshot(Review review) {
        this(review.getReviewId(), review.getOffer(), review.getValue(), review.getDate());
    }
}
