package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.Const;
import org.shareio.backend.core.model.Address;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.vo.Category;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferModifyDto;
import org.shareio.backend.core.usecases.port.in.ModifyOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.core.usecases.port.out.UpdateOfferChangeMetadataCommandInterface;
import org.shareio.backend.core.usecases.util.LocationCalculator;
import org.shareio.backend.exceptions.LocationCalculationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ModifyOfferUseCaseService implements ModifyOfferUseCaseInterface {
    GetOfferDaoInterface getOfferDaoInterface;
    UpdateOfferChangeMetadataCommandInterface updateOfferChangeMetadataCommandInterface;

    @Override
    public void modifyOffer(OfferModifyDto offerModifyDto) throws LocationCalculationException, IOException, InterruptedException {
        OfferGetDto offerGetDto = getOfferDaoInterface.getOfferDto(offerModifyDto.offerId()).orElseThrow(NoSuchElementException::new);
        Offer offer = Optional.of(offerGetDto).map(Offer::fromDto).get();
        Address address = new Address(null, offerModifyDto.addressSaveDto().country(), offerModifyDto.addressSaveDto().region(), offerModifyDto.addressSaveDto().city(), offerModifyDto.addressSaveDto().street(), offerModifyDto.addressSaveDto().houseNumber(), offerModifyDto.addressSaveDto().flatNumber(), offerModifyDto.addressSaveDto().postCode(), new Location(Const.defaultAddressCenterLat, Const.defaultAddressCenterLon));
        offer.setTitle(offerModifyDto.title());
        offer.setDescription(offerModifyDto.description());
        offer.setAddress(address);
        offer.getAddress().setLocation(LocationCalculator.getLocationFromAddress(offerModifyDto.addressSaveDto().country(), offerModifyDto.addressSaveDto().city(), offerModifyDto.addressSaveDto().street(), offerModifyDto.addressSaveDto().houseNumber()));
        offer.setCondition(Condition.valueOf(offerModifyDto.title()));
        offer.setCategory(Category.valueOf(offerModifyDto.category()));
        updateOfferChangeMetadataCommandInterface.updateOfferMetadata(offer.toSnapshot());
    }
}
