package org.shareio.backend.infrastructure.dbadapter.adapters;

import org.shareio.backend.core.usecases.port.dto.AddressGetDto;
import org.shareio.backend.core.usecases.port.dto.LocationGetDto;
import org.shareio.backend.core.usecases.port.out.GetAddressDaoInterface;
import org.shareio.backend.core.usecases.port.out.GetLocationDaoInterface;
import org.shareio.backend.infrastructure.dbadapter.entities.AddressEntity;
import org.shareio.backend.infrastructure.dbadapter.mappers.AddressDatabaseMapper;
import org.shareio.backend.infrastructure.dbadapter.repositories.AddressRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddressAdapter implements GetAddressDaoInterface, GetLocationDaoInterface {
    final AddressRepository addressRepository;

    public AddressAdapter(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Optional<AddressGetDto> getAddressDto(UUID id) {
        Optional<AddressEntity> addressEntity = addressRepository.findByAddressId(id);
        if (addressEntity.isEmpty()) {
            throw new NoSuchElementException();
        }
        return addressEntity.map(AddressDatabaseMapper::toDto);
    }


    @Override
    public Optional<LocationGetDto> getLocationDto(UUID id) {
        Optional<AddressEntity> addressEntity = addressRepository.findByAddressId(id);
        if (addressEntity.isEmpty()) {
            throw new NoSuchElementException();
        }
        return addressEntity.map(AddressDatabaseMapper::toLocationDto);
    }
}
