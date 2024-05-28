package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.in.GetOwnerReviewCountUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetOwnerReviewCountUseCaseService implements GetOwnerReviewCountUseCaseInterface {

    GetAllOffersDaoInterface getAllOffersDaoInterface;

    @Override
    public Integer getUserReviewCount(UUID ownerId) {
        List<OfferGetDto> offerGetDtoList = getAllOffersDaoInterface.getAllOffers();
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

        return Math.toIntExact(offerGetDtoList
                .stream()
                .map(Offer::fromDto)
                .filter(offer -> offer.getStatus().equals(Status.FINISHED))
                .filter(offer -> Objects.equals(offer.getOwner().getUserId().getId(), ownerId))
                .filter(offer -> Objects.nonNull(offer.getReview()))
                .count());

    }
}
