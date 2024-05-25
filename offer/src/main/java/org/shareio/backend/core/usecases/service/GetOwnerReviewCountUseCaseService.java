package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.usecases.port.in.GetOwnerReviewCountUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetOwnerReviewCountUseCaseService implements GetOwnerReviewCountUseCaseInterface {

    GetAllOffersDaoInterface getAllOffersDaoInterface;

    @Override
    public Long getUserReviewCount(UUID ownerId) {
        return getAllOffersDaoInterface
                .getAllOffers()
                .stream()
                .map(Offer::fromDto)
                .filter(offer -> Objects.equals(offer.getOwner().getUserId().getId(), ownerId))
                .filter(offer -> Objects.nonNull(offer.getReview()))
                .count();

    }
}
