package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shareio.backend.Const;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.in.PeriodicOfferReservationTimeoutUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateOfferDereserveOfferCommandInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class PeriodicOfferReservationTimeoutReservationTimeoutUseCaseService implements PeriodicOfferReservationTimeoutUseCaseInterface {

    GetAllOffersDaoInterface getAllOffersDaoInterface;
    UpdateOfferDereserveOfferCommandInterface updateOfferDereserveOfferCommandInterface;

    @Override
    public void periodicOfferReservationTimeoutHandler() {
        List<OfferGetDto> offerGetDtoList = getAllOffersDaoInterface.getAllOffers();
        offerGetDtoList = offerGetDtoList.stream().filter(offer -> {
            try {
                OfferValidator.validateOffer(offer);
                return true;
            } catch (MultipleValidationException e) {
                return false;
            }
        }).toList();
        List<Offer> offerToDereserveList = offerGetDtoList
                .stream()
                .map(Offer::fromDto)
                .filter(offer -> offer.getStatus().equals(Status.RESERVED))
                .filter(offer -> LocalDateTime.now().isAfter(offer.getReservationDate().plus(Const.OFFER_RESERVATION_DURATION)))
                .toList();
        offerToDereserveList.forEach(offer -> {
            offer.setReceiver(null);
            offer.setStatus(Status.CREATED);
            offer.setReservationDate(null);
        });
        offerToDereserveList.stream()
                .map(Offer::toSnapshot)
                .forEach(offer -> {
                    updateOfferDereserveOfferCommandInterface.dereserveOffer(offer);
                    log.error("dereserved offer: {}", offer);
                });

    }
}
