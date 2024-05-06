package org.shareio.backend.infrastructure.dbadapter.adapters;

import org.shareio.backend.core.usecases.port.dto.ReviewGetDto;
import org.shareio.backend.core.usecases.port.out.GetAllReviewsDaoInterface;
import org.shareio.backend.core.usecases.port.out.RemoveReviewCommandInterface;
import org.shareio.backend.infrastructure.dbadapter.entities.ReviewEntity;
import org.shareio.backend.infrastructure.dbadapter.mappers.ReviewMapper;
import org.shareio.backend.infrastructure.dbadapter.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class ReviewAdapter implements GetAllReviewsDaoInterface, RemoveReviewCommandInterface {

    final ReviewRepository reviewRepository;

    public ReviewAdapter(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }


    @Override
    public List<ReviewGetDto> getAllReviews() {
        List<ReviewEntity> reviewEntityList = (ArrayList<ReviewEntity>)reviewRepository.findAll();
        return reviewEntityList.stream().map(ReviewMapper::toDto).toList();
    }

    @Override
    public void removeReview(UUID reviewId) {
        reviewRepository.delete(reviewRepository.findByReviewId(reviewId).orElseThrow());
    }
}
