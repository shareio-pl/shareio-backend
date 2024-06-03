package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.in.GetOffersByReceiverAndStatusUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class GetOffersByReceiverAndStatusUseCaseService implements GetOffersByReceiverAndStatusUseCaseInterface {

    GetAllOffersDaoInterface getAllOffersDaoInterface;

    @Override
    public List<UUID> getReservedOffersByRecieverAndStatus(UUID recieverId, Status status) {
        List<OfferGetDto> offerGetDtoList = getAllOffersDaoInterface.getAllOffers();
        offerGetDtoList = offerGetDtoList.stream().filter(offer -> {
            try {
                OfferValidator.validateOffer(offer);
                return true;
            } catch (MultipleValidationException e) {
                return false;
            }
        }).toList();

        List<Offer> offerList = offerGetDtoList
                .stream()
                .map(Offer::fromDto)
                .toList();

        return offerList
                .stream()
                .filter(offer-> offer.getStatus().equals(status))
                .filter(offer -> offer.getReceiver().getUserId().getId().equals(recieverId))
                .map(Offer::toSnapshot)
                .map(offer->offer.offerId().getId())
                .toList();
    }
}
