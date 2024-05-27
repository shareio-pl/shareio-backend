package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.in.GetNewestOffersUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class GetNewestOffersUseCaseService implements GetNewestOffersUseCaseInterface {

    GetAllOffersDaoInterface getAllOffersDaoInterface;

    @Override
    public List<UUID> getNewestOffers() {
        List<Offer> twoNewestOfferList;
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
                .filter(offer -> !offer.getStatus().equals(Status.CANCELED))
                .sorted(Comparator.comparing(Offer::getCreationDate))
                .toList();
        if(offerList.size() >=2) {
            twoNewestOfferList = List.of(offerList.get(0), offerList.get(1));

        }
        else throw new IllegalArgumentException();

        offerList =
                offerList
                .stream()
                .filter(offer -> offer.getCreationDate().plusDays(1).isBefore(LocalDateTime.now()))
                .toList();
        if(offerList.isEmpty()){
            return twoNewestOfferList
                    .stream()
                    .map(Offer::toSnapshot)
                    .map(offer -> offer.offerId().getId())
                    .toList();
        }
        else{
            return offerList .stream()
                    .map(Offer::toSnapshot)
                    .map(offer -> offer.offerId().getId())
                    .toList();

        }
    }
}

