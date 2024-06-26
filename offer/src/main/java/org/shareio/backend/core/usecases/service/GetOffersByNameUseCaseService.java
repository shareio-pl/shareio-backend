package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.in.GetOffersByNameUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOffersByNameDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetOffersByNameUseCaseService implements GetOffersByNameUseCaseInterface {
    GetOffersByNameDaoInterface getOffersByNameDaoInterface;

    @Override
    public List<UUID> getOfferResponseDtoListByName(String name) {
        List<OfferGetDto> getOfferDtoList = getOffersByNameDaoInterface.getOffersByName(name);
        getOfferDtoList = getOfferDtoList.stream().filter(offer -> {
            try {
                OfferValidator.validateOffer(offer);
                return true;
            } catch (MultipleValidationException e) {
                return false;
            }
        }).toList();

        return getOfferDtoList
                .stream()
                .map(Offer::fromDto)
                .filter(offer -> offer.getStatus().equals(Status.CREATED))
                .map(Offer::toSnapshot)
                .map(offer -> offer.offerId().getId())
                .toList();

    }
}
