package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.in.GetOffersByUserUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOffersByUserDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetOffersByUserUseCaseService implements GetOffersByUserUseCaseInterface {
    GetOffersByUserDaoInterface getOffersByUserDaoInterface;

    @Override
    public List<UUID> getOfferResponseDtoListByUser(UUID id) throws NoSuchElementException {
        List<OfferGetDto> getOfferDtoList = getOffersByUserDaoInterface.getOffersByUser(id);
        getOfferDtoList.forEach(offer ->
        {
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

        if (getOfferDtoList.isEmpty()) {
            throw new NoSuchElementException("No offers found for this user!");
        }

        return getOfferDtoList
                .stream()
                .map(Offer::fromDto)
                .filter(offer -> !offer.getStatus().equals(Status.CANCELED))
                .map(Offer::toSnapshot)
                .map(offer -> offer.offerId().getId())
                .toList();
    }
}
