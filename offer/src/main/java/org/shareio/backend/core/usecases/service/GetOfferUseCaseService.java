package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.LocationResponseDto;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferResponseDto;
import org.shareio.backend.core.usecases.port.in.GetLocationUseCaseInterface;
import org.shareio.backend.core.usecases.port.in.GetOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.infrastructure.mappers.OfferInfrastructureMapper;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetOfferUseCaseService implements GetOfferUseCaseInterface {
    GetUserProfileDaoInterface getUserProfileDaoInterface;
    GetLocationUseCaseInterface getLocationUseCaseInterface;
    GetOfferDaoInterface getOfferDaoInterface;

    @Override
    public OfferResponseDto getOfferResponseDto(UUID id, UUID userId, Integer reviewCount, Double averageUserReviewValue) throws MultipleValidationException {
        Optional<OfferGetDto> getOfferDto = getOfferDaoInterface.getOfferDto(id);
        OfferValidator.validateOffer(getOfferDto.orElseThrow());
        Location location;
        Offer offer = getOfferDto.map(Offer::fromDto).orElseThrow(NoSuchElementException::new);
        try{
            UUID userAddressId = getUserProfileDaoInterface.getUserDto(userId).orElseThrow(NoSuchElementException::new).addressId();
            LocationResponseDto locationResponseDto = getLocationUseCaseInterface
                    .getLocationResponseDto(userAddressId);
            location = new Location(locationResponseDto.latitude(), locationResponseDto.longitude());
        } catch(NoSuchElementException e){
            location = null;
        }


        if(offer.getStatus().equals(Status.CANCELED)){
            throw new NoSuchElementException();
        }
        OfferSnapshot offerSnapshot = offer.toSnapshot();
        return OfferInfrastructureMapper.toDto(offerSnapshot, location, reviewCount, averageUserReviewValue);
    }
}
