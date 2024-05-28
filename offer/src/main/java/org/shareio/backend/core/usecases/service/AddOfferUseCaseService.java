package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.User;
import org.shareio.backend.core.model.vo.PhotoId;
import org.shareio.backend.core.usecases.port.dto.OfferSaveDto;
import org.shareio.backend.core.usecases.port.dto.OfferSaveResponseDto;
import org.shareio.backend.core.usecases.port.in.AddOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.*;
import org.shareio.backend.core.usecases.util.LocationCalculator;
import org.shareio.backend.exceptions.LocationCalculationException;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class AddOfferUseCaseService implements AddOfferUseCaseInterface {

    GetUserProfileDaoInterface getUserProfileDaoInterface;
    SaveOfferCommandInterface saveOfferCommandInterface;

    @Override
    public OfferSaveResponseDto addOffer(OfferSaveDto offerSaveDto, UUID photoId) throws LocationCalculationException, IOException, InterruptedException, MultipleValidationException {
        OfferValidator.validateOffer(offerSaveDto);
        Offer offer = Optional.of(offerSaveDto).map(Offer::fromDto).orElseThrow(NoSuchElementException::new);
        User owner = getUserProfileDaoInterface.getUserDto(offerSaveDto.ownerId()).map(User::fromDto).orElseThrow(NoSuchElementException::new);
        offer.setOwner(owner);
        offer.setPhotoId(new PhotoId(photoId));
        offer.getAddress().setLocation(LocationCalculator.getLocationFromAddress(offerSaveDto.country(), offerSaveDto.city(), offerSaveDto.street(), offerSaveDto.houseNumber()));
        OfferSnapshot offerSnapshot = offer.toSnapshot();
        saveOfferCommandInterface.saveOffer(offerSnapshot);
        return new OfferSaveResponseDto(offer.getOfferId().getId());
    }
}
