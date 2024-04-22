package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferResponseDto;
import org.shareio.backend.core.usecases.port.in.GetOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.infrastructure.mappers.OfferInfrastructureMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetOfferUseCaseService implements GetOfferUseCaseInterface {
    GetOfferDaoInterface getOfferDaoInterface;


    @Override
    public OfferResponseDto getOfferResponseDto(UUID id) throws MultipleValidationException {
        Optional<OfferGetDto> getOfferDto = getOfferDaoInterface.getOfferDto(id);
        OfferValidator.validateOffer(getOfferDto.orElseThrow());
        return Optional.of(getOfferDto.map(Offer::fromDto).get().toSnapshot()).map(OfferInfrastructureMapper::toDto).get();
    }
}
