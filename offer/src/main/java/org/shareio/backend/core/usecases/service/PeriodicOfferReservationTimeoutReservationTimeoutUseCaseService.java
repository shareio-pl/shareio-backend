package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shareio.backend.Const;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.in.PeriodicOfferReservationTimeoutUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetAllOffersByStatusDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateOfferDereserveOfferCommandInterface;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class PeriodicOfferReservationTimeoutReservationTimeoutUseCaseService implements PeriodicOfferReservationTimeoutUseCaseInterface {

    GetAllOffersByStatusDaoInterface getAllOffersByStatusDaoInterface;
    UpdateOfferDereserveOfferCommandInterface updateOfferDereserveOfferCommandInterface;

    @Override
    public void periodicOfferReservationTimeoutHandler() {
        List<OfferGetDto> offerGetDtoList = getAllOffersByStatusDaoInterface.getAllOffersByStatus(Status.RESERVED);
        List<Offer> offerToDereserveList = offerGetDtoList.stream().map(Offer::fromDto)
                .filter(offer -> LocalDateTime.now().isAfter(offer.getReservationDate().plus(Const.offerReservationDuration)))
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