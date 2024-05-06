package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.usecases.port.dto.RemoveResponseDto;
import org.shareio.backend.core.usecases.port.in.RemoveOffersForUserUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.shareio.backend.core.usecases.port.out.RemoveOfferCommandInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RemoveOffersForUserUseCaseService implements RemoveOffersForUserUseCaseInterface {
    GetAllOffersDaoInterface getAllOffersDaoInterface;
    RemoveOfferCommandInterface removeOfferCommandInterface;
    @Override
    public RemoveResponseDto removeOffersForUser(UUID userId, RemoveResponseDto removeResponseDto) {
        List<Offer> offerList = getAllOffersDaoInterface.getAllOffers().stream().map(Offer::fromDto).toList();
        offerList = offerList.stream().filter(offer -> Objects.equals(offer.getOwner().getUserId().getId() , userId)).toList();
        for (Offer offer : offerList) {
            // TODO: REMOVE PHOTO BY SHAREIO-IMAGE
            removeOfferCommandInterface.removeOffer(offer.getOfferId().getId());
            removeResponseDto.setDeletedOfferCount(removeResponseDto.getDeletedOfferCount() + 1);
        }
        removeResponseDto.setDeletedAddressCount(removeResponseDto.getDeletedAddressCount()+offerList.size());
        return removeResponseDto;
    }
}
