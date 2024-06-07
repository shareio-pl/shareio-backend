package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.json.JSONException;
import org.shareio.backend.Const;
import org.shareio.backend.core.model.Address;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Category;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferModifyDto;
import org.shareio.backend.core.usecases.port.in.ModifyOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateOfferChangeMetadataCommandInterface;
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
public class ModifyOfferUseCaseService implements ModifyOfferUseCaseInterface {
    GetOfferDaoInterface getOfferDaoInterface;
    UpdateOfferChangeMetadataCommandInterface updateOfferChangeMetadataCommandInterface;

    @Override
    public void modifyOffer(UUID offerId, OfferModifyDto offerModifyDto) throws MultipleValidationException {
        OfferGetDto offerGetDto = getOfferDaoInterface.getOfferDto(offerId).orElseThrow(NoSuchElementException::new);
        OfferValidator.validateOffer(offerGetDto);
        Offer offer = Optional.of(offerGetDto).map(Offer::fromDto).orElseThrow(NoSuchElementException::new);
        if(offer.getStatus().equals(Status.CANCELED)){
            throw new NoSuchElementException();
        }
        Address address = new Address(null, offerModifyDto.addressSaveDto().country(), offerModifyDto.addressSaveDto().region(), offerModifyDto.addressSaveDto().city(), offerModifyDto.addressSaveDto().street(), offerModifyDto.addressSaveDto().houseNumber(), offerModifyDto.addressSaveDto().flatNumber(), offerModifyDto.addressSaveDto().postCode(), new Location(Const.DEFAULT_ADDRESS_CENTER_LAT, Const.DEFAULT_ADDRESS_CENTER_LON));
        offer.setTitle(offerModifyDto.title());
        offer.setDescription(offerModifyDto.description());
        offer.setAddress(address);
        try{
            offer.getAddress().setLocation(LocationCalculator.getLocationFromAddress(offerModifyDto.addressSaveDto().country(), offerModifyDto.addressSaveDto().city(), offerModifyDto.addressSaveDto().street(), offerModifyDto.addressSaveDto().houseNumber()));
        }
        catch(LocationCalculationException | IOException | InterruptedException | JSONException e){
            Thread.currentThread().interrupt();
            offer.getAddress().setLocation(new Location(0.0, 0.0));
        }
        offer.setCondition(Condition.valueOf(offerModifyDto.title()));
        offer.setCategory(Category.valueOf(offerModifyDto.category()));
        updateOfferChangeMetadataCommandInterface.updateOfferMetadata(offer.toSnapshot());
    }
}
