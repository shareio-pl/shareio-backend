package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.model.Review;
import org.shareio.backend.core.model.vo.ReviewId;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferReviewDto;
import org.shareio.backend.core.usecases.port.in.AddReviewUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateOfferSaveReviewCommandInterface;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AddReviewUseCaseService implements AddReviewUseCaseInterface {

    GetOfferDaoInterface getOfferDaoInterface;
    GetUserProfileDaoInterface getUserProfileDaoInterface;
    UpdateOfferSaveReviewCommandInterface updateOfferSaveReviewCommandInterface;

    @Override
    public UUID addReview(OfferReviewDto offerReviewDto) {
        UUID reviewId = UUID.randomUUID();
        OfferGetDto offerGetDto = getOfferDaoInterface.getOfferDto(offerReviewDto.offerId()).orElseThrow(NoSuchElementException::new);
        Offer offer = Optional.of(offerGetDto).map(Offer::fromDto).get();
        Review review = new Review(new ReviewId(reviewId), offerReviewDto.reviewValue(), offerReviewDto.reviewDate());
        offer.setReview(review);
        OfferSnapshot offerSnapshot = offer.toSnapshot();
        updateOfferSaveReviewCommandInterface.updateOfferAddReview(offerSnapshot);
        return reviewId;
    }
}
