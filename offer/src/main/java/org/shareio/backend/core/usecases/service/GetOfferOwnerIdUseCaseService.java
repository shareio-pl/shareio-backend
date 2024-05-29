package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.in.GetOfferOwnerIdUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class GetOfferOwnerIdUseCaseService implements GetOfferOwnerIdUseCaseInterface {
    GetOfferDaoInterface getOfferDaoInterface;
    @Override
    public UUID getOfferOwnerId(UUID offerId) throws MultipleValidationException {
        Optional<OfferGetDto> getOfferDto = getOfferDaoInterface.getOfferDto(offerId);
        OfferValidator.validateOffer(getOfferDto.orElseThrow());
        Offer offer = getOfferDto.map(Offer::fromDto).orElseThrow(NoSuchElementException::new);
        return Optional.of(offer).map(Offer::toSnapshot).map(offerLambda->offerLambda.owner().userId().getId()).orElseThrow(NoSuchElementException::new);
    }
}
