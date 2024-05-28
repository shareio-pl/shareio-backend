package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferResponseDto;
import org.shareio.backend.core.usecases.port.in.GetOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.infrastructure.mappers.OfferInfrastructureMapper;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetOfferUseCaseService implements GetOfferUseCaseInterface {
    GetOfferDaoInterface getOfferDaoInterface;

    @Override
    public OfferResponseDto getOfferResponseDto(UUID id, Integer reviewCount, Double averageUserReviewValue) throws MultipleValidationException {
        Optional<OfferGetDto> getOfferDto = getOfferDaoInterface.getOfferDto(id);
        OfferValidator.validateOffer(getOfferDto.orElseThrow());
        Offer offer = getOfferDto.map(Offer::fromDto).orElseThrow(NoSuchElementException::new);
        if(offer.getStatus().equals(Status.CANCELED)){
            throw new NoSuchElementException();
        }
        OfferSnapshot offerSnapshot = offer.toSnapshot();
        return OfferInfrastructureMapper.toDto(offerSnapshot, reviewCount, averageUserReviewValue);
    }
}
