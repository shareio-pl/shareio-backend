package org.shareio.backend.infrastructure.controller;


import lombok.AllArgsConstructor;

import org.shareio.backend.Const;
import org.shareio.backend.core.usecases.port.in.PeriodicOfferReservationTimeoutUseCaseInterface;
import org.shareio.backend.security.RequestLogHandler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class OfferScheduledController {

    PeriodicOfferReservationTimeoutUseCaseInterface periodicOfferReservationTimeoutUseCaseInterface;

    @Scheduled(fixedRate = Const.offerReservationCheckRate)
    public void dereserveOffers() {
        RequestLogHandler.handlePeriodicTask();
        periodicOfferReservationTimeoutUseCaseInterface.periodicOfferReservationTimeoutHandler();
    }
}
