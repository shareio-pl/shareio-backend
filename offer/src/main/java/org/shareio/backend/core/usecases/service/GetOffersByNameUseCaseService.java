package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferResponseDto;
import org.shareio.backend.core.usecases.port.in.GetOffersByNameUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOffersByNameDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.infrastructure.mappers.OfferInfrastructureMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetOffersByNameUseCaseService implements GetOffersByNameUseCaseInterface {
    GetOffersByNameDaoInterface getOffersByNameDaoInterface;

    @Override
    public List<UUID> getOfferResponseDtoListByName(String name) {
        List<OfferGetDto> getOfferDtoList = getOffersByNameDaoInterface.getOffersByName(name);
        getOfferDtoList.forEach(offer -> {
            try {
                OfferValidator.validateOffer(offer);
            } catch (MultipleValidationException e) {
                try {
                    throw new MultipleValidationException(e.getMessage(), e.getErrorMap());
                } catch (MultipleValidationException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        List<OfferSnapshot> offerSnapshotList = getOfferDtoList.stream().map(Offer::fromDto).map(Offer::toSnapshot).toList();
        List<UUID> offerUUIDList = new ArrayList<>();
        offerSnapshotList.forEach(offer -> offerUUIDList.add(offer.offerId().getId()));
        return offerUUIDList;
    }
}
