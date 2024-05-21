package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferModifyDto;
import org.shareio.backend.core.usecases.port.in.ModifyOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ModifyOfferUseCaseService implements ModifyOfferUseCaseInterface {
    GetOfferDaoInterface getOfferDaoInterface;

    @Override
    public void modifyOffer(OfferModifyDto offerModifyDto) {
        OfferGetDto offerGetDto = getOfferDaoInterface.getOfferDto(offerModifyDto.offerId()).orElseThrow(NoSuchElementException::new);
        Offer offer = Optional.of(offerGetDto).map(Offer::fromDto).get();
    }
}
