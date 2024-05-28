package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.Const;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.in.GetNewestOffersUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class GetNewestOffersUseCaseService implements GetNewestOffersUseCaseInterface {

    GetAllOffersDaoInterface getAllOffersDaoInterface;

    @Override
    public List<UUID> getNewestOffers() {
        List<Offer> backupNewestOfferList = new ArrayList<>();
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
                .filter(offer -> offer.getStatus().equals(Status.CREATED))
                .sorted(Comparator.comparing(Offer::getCreationDate))
                .toList();
        if(offerList.size() >= Const.minOfferListSize) {
            for(int i=0; i<Const.minOfferListSize;i++){
                backupNewestOfferList.add(offerList.get(i));
            }

        }
        else throw new IllegalArgumentException();
        offerList =
                offerList
                .stream()
                .filter(offer -> {
                    Duration d = Duration.between(offer.getCreationDate(),LocalDateTime.now());
                    return d.toDays() <= 1;
                })
                .toList();
        if(offerList.isEmpty()){
            return backupNewestOfferList
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

