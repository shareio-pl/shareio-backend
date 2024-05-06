package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Review;
import org.shareio.backend.core.usecases.port.dto.RemoveResponseDto;
import org.shareio.backend.core.usecases.port.dto.ReviewGetDto;
import org.shareio.backend.core.usecases.port.in.RemoveReviewsForOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetAllReviewsDaoInterface;
import org.shareio.backend.core.usecases.port.out.RemoveReviewCommandInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RemoveReviewsForOfferUseCaseService implements RemoveReviewsForOfferUseCaseInterface {
    GetAllReviewsDaoInterface getAllReviewsDaoInterface;
    RemoveReviewCommandInterface removeReviewCommandInterface;
    @Override
    public RemoveResponseDto removeReviewsForOffer(UUID offerId, RemoveResponseDto removeResponseDto) {
        List<ReviewGetDto> reviewGetDtoList = getAllReviewsDaoInterface.getAllReviews();
        List<Review> reviewList = reviewGetDtoList.stream().map(Review::fromDto).toList();
        reviewList = reviewList.stream().filter(review -> Objects.equals(review.getOffer().getOfferId().getId(), offerId)).toList();
        for (Review review : reviewList) {
            removeReviewCommandInterface.removeReview(review.getReviewId().getId());
            removeResponseDto.setDeletedReviewCount(removeResponseDto.getDeletedReviewCount() + 1);
        }
        return removeResponseDto;
    }
}
