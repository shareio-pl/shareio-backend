package org.shareio.backend.core.usecases.service;

import org.shareio.backend.core.model.Address;
import org.shareio.backend.core.model.AddressValidator;
import org.shareio.backend.core.usecases.port.dto.AddressGetDto;
import org.shareio.backend.core.usecases.port.dto.AddressResponseDto;
import org.shareio.backend.core.usecases.port.in.GetAddressUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetAddressDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.infrastructure.mappers.AddressInfrastructureMapper;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class GetAddressUseCaseService implements GetAddressUseCaseInterface {
    GetAddressDaoInterface getAddressDaoInterface;


    @Override
    public AddressResponseDto getAddressResponseDto(UUID id) throws MultipleValidationException, NoSuchElementException {
        Optional<AddressGetDto> getAddressDto = getAddressDaoInterface.getAddressDto(id);
        AddressValidator.validateAddress(getAddressDto.orElseThrow());
        return Optional.of(getAddressDto.map(Address::fromDto).get().toSnapshot()).map(AddressInfrastructureMapper::toDto).get();
    }
}
