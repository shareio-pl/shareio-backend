package org.shareio.backend.core.usecases.service;

import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferResponseDto;
import org.shareio.backend.core.usecases.port.in.GetOffersByNameUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOffersByNameDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.infrastructure.mappers.OfferInfrastructureMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GetOffersByNameUseCaseService implements GetOffersByNameUseCaseInterface {
    GetOffersByNameDaoInterface getOffersByNameDaoInterface;

    @Override
    public List<UUID> getOfferResponseDtoListByName(String name) throws MultipleValidationException {
        List<OfferGetDto> getOfferDtoList = getOffersByNameDaoInterface.getOffersByName(name);
        getOfferDtoList.forEach(offer -> {
            try {
                OfferValidator.validateOffer(offer);
            } catch (MultipleValidationException e) {
                throw new MultipleValidationException(e.getMessage(), e.getErrorMap());
            }
        });
        List<OfferSnapshot> offerSnapshotList = getOfferDtoList.stream().map(Offer::fromDto).map(Offer::toSnapshot).toList();
        List<UUID> offerUUIDList = new ArrayList<>();
        offerSnapshotList.forEach(offer -> offerUUIDList.add(offer.getOfferId()));
        return offerUUIDList;
    }
}
