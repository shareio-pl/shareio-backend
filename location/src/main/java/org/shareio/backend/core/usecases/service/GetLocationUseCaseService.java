package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.AddressValidator;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.usecases.port.dto.LocationGetDto;
import org.shareio.backend.core.usecases.port.dto.LocationResponseDto;
import org.shareio.backend.core.usecases.port.in.GetLocationUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetLocationDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.infrastructure.mappers.AddressInfrastructureMapper;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetLocationUseCaseService implements GetLocationUseCaseInterface {
    GetLocationDaoInterface getLocationDaoInterface;


    @Override
    public LocationResponseDto getLocationResponseDto(UUID id) throws MultipleValidationException, NoSuchElementException {
        Optional<LocationGetDto> getLocationDto = getLocationDaoInterface.getLocationDto(id);
        AddressValidator.validateLocation(getLocationDto.orElseThrow());
        return Optional.of(getLocationDto.map(Location::fromDto).get().toSnapshot()).map(AddressInfrastructureMapper::toDto).get();
    }
}
