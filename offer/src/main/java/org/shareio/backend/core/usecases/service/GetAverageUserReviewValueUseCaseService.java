package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.in.GetAverageUserReviewValueUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
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
        List<OfferGetDto> offerGetDtoList =  getAllOffersDaoInterface.getAllOffers();
        offerGetDtoList = offerGetDtoList.stream()
                .filter(offer -> {
                    try {
                        OfferValidator.validateOffer(offer);
                        return true;
                    } catch (MultipleValidationException e) {
                        return false;
                    }
                })
                .toList();
        List<Offer> offerList = offerGetDtoList
                .stream()
                .map(Offer::fromDto)
                .filter(offer -> Objects.equals(offer.getOwner().getUserId().getId(), userId))
                .filter(offer -> Objects.nonNull(offer.getReview()))
                .filter(offer -> !offer.getStatus().equals(Status.CANCELED))
                .toList();

        return offerList.stream().map(offer -> offer.getReview().getValue())
                .reduce(0.0, Double::sum) / offerList.size();
    }
}
