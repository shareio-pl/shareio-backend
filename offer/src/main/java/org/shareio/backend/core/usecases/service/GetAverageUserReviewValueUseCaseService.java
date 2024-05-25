package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.usecases.port.in.GetAverageUserReviewValueUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetAverageUserReviewValueUseCaseService implements GetAverageUserReviewValueUseCaseInterface {

    GetAllOffersDaoInterface getAllOffersDaoInterface;

    @Override
    public Double getAverageUserReviewValue(UUID userId){
        List<Offer> offerGetDtoList = getAllOffersDaoInterface
                .getAllOffers()
                .stream()
                .map(Offer::fromDto)
                .filter(offer -> Objects.equals(offer.getOwner().getUserId().getId(), userId))
                .filter(offer -> Objects.nonNull(offer.getReview()))
                .toList();

        return offerGetDtoList.stream().map(offer -> offer.getReview().getValue())
                .reduce(0.0, Double::sum) / offerGetDtoList.size();
    }
}
