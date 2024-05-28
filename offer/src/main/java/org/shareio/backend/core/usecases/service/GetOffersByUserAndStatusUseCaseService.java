package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.in.GetOffersByUserAndStatusUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOffersByUserDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetOffersByUserAndStatusUseCaseService implements GetOffersByUserAndStatusUseCaseInterface {
    GetOffersByUserDaoInterface getOffersByUserDaoInterface;

    @Override
    public List<UUID> getOfferResponseDtoListByUser(UUID id, Status status) throws NoSuchElementException {
        List<OfferGetDto> getOfferDtoList = getOffersByUserDaoInterface.getOffersByUser(id);
        getOfferDtoList = getOfferDtoList.stream().filter(offer -> {
            try {
                OfferValidator.validateOffer(offer);
                return true;
            } catch (MultipleValidationException e) {
                return false;
            }
        }).toList();

        if (getOfferDtoList.isEmpty()) {
            throw new NoSuchElementException("No offers found for this user!");
        }

        return getOfferDtoList
                .stream()
                .map(Offer::fromDto)
                .filter(offer -> offer.getStatus().equals(status))
                .map(Offer::toSnapshot)
                .map(offer -> offer.offerId().getId())
                .toList();
    }
}
