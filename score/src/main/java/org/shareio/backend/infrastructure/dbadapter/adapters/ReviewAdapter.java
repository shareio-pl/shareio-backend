package org.shareio.backend.infrastructure.dbadapter.adapters;

import org.shareio.backend.core.usecases.port.out.RemoveReviewCommandInterface;
import org.shareio.backend.infrastructure.dbadapter.repositories.ReviewRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class ReviewAdapter implements RemoveReviewCommandInterface {

    final ReviewRepository reviewRepository;

    public ReviewAdapter(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }


    @Override
    @Modifying
    public void removeReview(UUID reviewId) {
        reviewRepository.delete(reviewRepository.findByReviewId(reviewId).orElseThrow());
    }
}
