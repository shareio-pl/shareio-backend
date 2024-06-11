package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferEndDto;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.in.FinishOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateOfferFinishOfferCommandInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class FinishOfferUseCaseService implements FinishOfferUseCaseInterface {

    GetOfferDaoInterface getOfferDaoInterface;
    UpdateOfferFinishOfferCommandInterface updateOfferFinishOfferCommandInterface;

    @Override
    public UUID finishOffer(OfferEndDto offerEndDto) throws MultipleValidationException {
        OfferGetDto offerGetDto = getOfferDaoInterface.getOfferDto(offerEndDto.offerId()).orElseThrow(NoSuchElementException::new);
        OfferValidator.validateOffer(offerGetDto);
        Offer offer = Optional.of(offerGetDto).map(Offer::fromDto).orElseThrow(NoSuchElementException::new);
        if (!offer.getStatus().equals(Status.RESERVED)) {
            throw new NoSuchElementException();
        }
        if (!offer.getReceiver().getUserId().getId().equals(offerEndDto.userId())) {
            throw new NoSuchElementException();
        }
        offer.setStatus(Status.FINISHED);
        updateOfferFinishOfferCommandInterface.finishOffer(offer.toSnapshot());
        return offer.getOfferId().getId();
    }
}
