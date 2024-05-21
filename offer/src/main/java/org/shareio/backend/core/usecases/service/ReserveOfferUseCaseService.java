package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.User;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferReserveDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.in.ReserveOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateOfferReserveOfferCommandInterface;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class ReserveOfferUseCaseService implements ReserveOfferUseCaseInterface {

    GetUserProfileDaoInterface getUserProfileDaoInterface;
    GetOfferDaoInterface getOfferDaoInterface;
    UpdateOfferReserveOfferCommandInterface updateOfferReserveOfferCommandInterface;

    @Override
    public UUID reserveOffer(OfferReserveDto offerReserveDto) {
        //TODO validate users reserved offer count
        //TODO validate offer status
        OfferGetDto offerGetDto = getOfferDaoInterface.getOfferDto(offerReserveDto.offerId()).orElseThrow(NoSuchElementException::new);
        Offer offer = Optional.of(offerGetDto).map(Offer::fromDto).get();
        UserProfileGetDto recieverProfileGetDto = getUserProfileDaoInterface.getUserDto(offerReserveDto.recieverId()).orElseThrow(NoSuchElementException::new);
        User reciever = Optional.of(recieverProfileGetDto).map(User::fromDto).get();
        offer.setReceiver(reciever);
        offer.setReservationDate(LocalDateTime.now());
        offer.setStatus(Status.RESERVED);
        updateOfferReserveOfferCommandInterface.reserveOffer(offer.toSnapshot());
        return offer.getOfferId().getId();
    }
}
