package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Address;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.model.User;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.usecases.port.dto.OfferSaveDto;
import org.shareio.backend.core.usecases.port.dto.OfferSaveResponseDto;
import org.shareio.backend.core.usecases.port.in.AddOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.*;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
import org.shareio.backend.infrastructure.dbadapter.mappers.OfferDatabaseMapper;
import org.shareio.backend.infrastructure.dbadapter.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AddOfferUseCaseService implements AddOfferUseCaseInterface {
    private final UserRepository userRepository;
    GetUserProfileDaoInterface getUserProfileDaoInterface;
    GetAddressDaoInterface getAddressDaoInterface;
    GetLocationDaoInterface getLocationDaoInterface;
    SaveOfferCommandInterface saveOfferCommandInterface;

    @Override
    public OfferSaveResponseDto addOffer(OfferSaveDto offerSaveDto) {
        //TODO Cleanup - move to adapter
        Offer offer = Optional.of(offerSaveDto).map(Offer::fromDto).get();
        User owner = getUserProfileDaoInterface.getUserDto(offerSaveDto.ownerId()).map(User::fromDto).get();
        Address ownerAddress = getAddressDaoInterface.getAddressDto(owner.getAddress().getAddressId().getId()).map(Address::fromDto).get();
        Location ownerLocation = getLocationDaoInterface.getLocationDto(owner.getAddress().getAddressId().getId()).map(Location::fromDto).get();
        ownerAddress.setLocation(ownerLocation);
        owner.setAddress(ownerAddress);
        offer.setOwner(owner);
        OfferSnapshot offerSnapshot = offer.toSnapshot();
        OfferEntity offerEntity = Optional.of(offerSnapshot).map(OfferDatabaseMapper::toEntity).get();
        UserEntity ownerEntity = userRepository.findByUserId(offerSnapshot.owner().userId().getId()).get();
        offerEntity.setOwner(ownerEntity);
        if (Objects.nonNull(offerSnapshot.receiver())) {
            UserEntity recieverEntity = userRepository.findByUserId(offerSnapshot.receiver().userId().getId()).get();
            offerEntity.setOwner(recieverEntity);
        }
        saveOfferCommandInterface.saveOffer(offerEntity);
        return new OfferSaveResponseDto(offer.getOfferId().getId());
    }
}
