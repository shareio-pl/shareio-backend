package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferEndDto;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.in.CancelOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateOfferCancelOfferCommandInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class CancelOfferUseCaseService implements CancelOfferUseCaseInterface {

    GetOfferDaoInterface getOfferDaoInterface;
    UpdateOfferCancelOfferCommandInterface updateOfferCancelOfferCommandInterface;

    @Override
    public UUID cancelOffer(OfferEndDto offerEndDto) throws MultipleValidationException {
        OfferGetDto offerGetDto = getOfferDaoInterface.getOfferDto(offerEndDto.offerId()).orElseThrow(NoSuchElementException::new);
        OfferValidator.validateOffer(offerGetDto);
        Offer offer = Optional.of(offerGetDto).map(Offer::fromDto).orElseThrow(NoSuchElementException::new);
        if (!(offer.getStatus().equals(Status.CREATED) || offer.getStatus().equals(Status.RESERVED))) {
            throw new NoSuchElementException();
        }
        if (!offer.getOwner().getUserId().getId().equals(offerEndDto.userId())) {
            throw new NoSuchElementException();
        }
        offer.setReceiver(null);
        offer.setReservationDate(null);
        offer.setStatus(Status.CANCELED);
        updateOfferCancelOfferCommandInterface.cancelOffer(offer.toSnapshot());
        return offer.getOfferId().getId();
    }
}
