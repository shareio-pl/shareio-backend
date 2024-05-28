package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.User;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferReserveDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileGetDto;
import org.shareio.backend.core.usecases.port.in.ReserveOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateOfferReserveOfferCommandInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
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
    public UUID reserveOffer(OfferReserveDto offerReserveDto) throws MultipleValidationException {
        OfferGetDto offerGetDto = getOfferDaoInterface.getOfferDto(offerReserveDto.offerId()).orElseThrow(NoSuchElementException::new);
        OfferValidator.validateOffer(offerGetDto);
        Offer offer = Optional.of(offerGetDto).map(Offer::fromDto).orElseThrow(NoSuchElementException::new);
        if(!offer.getStatus().equals(Status.CREATED)){
            throw new NoSuchElementException();
        }
        UserProfileGetDto recieverProfileGetDto = getUserProfileDaoInterface.getUserDto(offerReserveDto.recieverId()).orElseThrow(NoSuchElementException::new);
        User reciever = Optional.of(recieverProfileGetDto).map(User::fromDto).orElseThrow(NoSuchElementException::new);
        offer.setReceiver(reciever);
        offer.setReservationDate(LocalDateTime.now());
        offer.setStatus(Status.RESERVED);
        updateOfferReserveOfferCommandInterface.reserveOffer(offer.toSnapshot());
        return offer.getOfferId().getId();
    }
}
