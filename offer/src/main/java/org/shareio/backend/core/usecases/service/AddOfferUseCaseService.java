package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.model.User;
import org.shareio.backend.core.usecases.port.dto.OfferSaveDto;
import org.shareio.backend.core.usecases.port.dto.OfferSaveResponseDto;
import org.shareio.backend.core.usecases.port.in.AddOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.*;
import org.shareio.backend.core.usecases.util.LocationCalculator;
import org.shareio.backend.exceptions.LocationCalculationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AddOfferUseCaseService implements AddOfferUseCaseInterface {

    GetUserProfileDaoInterface getUserProfileDaoInterface;
    SaveOfferCommandInterface saveOfferCommandInterface;

    @Override
    public OfferSaveResponseDto addOffer(OfferSaveDto offerSaveDto) throws LocationCalculationException, IOException, InterruptedException {
        Offer offer = Optional.of(offerSaveDto).map(Offer::fromDto).get();
        User owner = getUserProfileDaoInterface.getUserDto(offerSaveDto.ownerId()).map(User::fromDto).orElseThrow(NoSuchElementException::new);
        offer.setOwner(owner);
        offer.getAddress().setLocation(LocationCalculator.getLocationFromAddress(offerSaveDto.addressSaveDto().country(), offerSaveDto.addressSaveDto().city(), offerSaveDto.addressSaveDto().street(), offerSaveDto.addressSaveDto().houseNumber()));
        OfferSnapshot offerSnapshot = offer.toSnapshot();
        saveOfferCommandInterface.saveOffer(offerSnapshot);
        return new OfferSaveResponseDto(offer.getOfferId().getId());
    }
}
