package org.shareio.backend.infrastructure.dbadapter.adapters;

import jakarta.transaction.Transactional;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.usecases.port.dto.OfferFullGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.out.*;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
import org.shareio.backend.infrastructure.dbadapter.mappers.AddressDatabaseMapper;
import org.shareio.backend.infrastructure.dbadapter.mappers.OfferDatabaseMapper;
import org.shareio.backend.infrastructure.dbadapter.repositories.OfferRepository;
import org.shareio.backend.infrastructure.dbadapter.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OfferAdapter implements GetOfferDaoInterface, GetAllOffersDaoInterface, GetOffersByNameDaoInterface, RemoveOfferCommandInterface, SaveOfferCommandInterface, GetOfferFullDaoInterface, UpdateOfferCommandInterface, GetOffersByUserDaoInterface {
    final OfferRepository offerRepository;
    final UserRepository userRepository;

    public OfferAdapter(OfferRepository offerRepository, UserRepository userRepository) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
    }

    // GET
    @Override
    public Optional<OfferGetDto> getOfferDto(UUID id) {
        Optional<OfferEntity> offerEntity = offerRepository.findByOfferId(id);
        if (offerEntity.isEmpty()) {
            throw new NoSuchElementException();
        }
        return offerEntity.map(OfferDatabaseMapper::toDto);
    }

    @Override
    public List<OfferGetDto> getAllOffers() {
        ArrayList<OfferEntity> offerList = (ArrayList<OfferEntity>) offerRepository.findAll();
        return offerList.stream().map(OfferDatabaseMapper::toDto).toList();
    }

    @Override
    public List<OfferGetDto> getOffersByName(String name) {
        ArrayList<OfferEntity> offerList = (ArrayList<OfferEntity>) offerRepository.findAll();
        return offerList.stream().filter(
                        offer -> offer.getTitle().toUpperCase().startsWith(name.toUpperCase()) || offer.getTitle().toUpperCase().endsWith(name.toUpperCase()))
                .map(OfferDatabaseMapper::toDto).toList();
    }

    @Override
    public Optional<OfferFullGetDto> getOfferFullDto(UUID id) {
        Optional<OfferEntity> offerEntity = offerRepository.findByOfferId(id);
        if (offerEntity.isEmpty()) {
            throw new NoSuchElementException();
        }
        return offerEntity.map(OfferDatabaseMapper::toFullDto);
    }

    @Override
    public List<OfferGetDto> getOffersByUser(UUID id) {
        ArrayList<OfferEntity> offerList = (ArrayList<OfferEntity>) offerRepository.findAll();
        return offerList.stream().filter(offer -> Objects.equals(offer.getOwner().getUserId(), id)).map(OfferDatabaseMapper::toDto).toList();
    }

    // SAVE

    @Override
    public void saveOffer(OfferSnapshot offerSnapshot) {

        OfferEntity offerEntity = Optional.of(offerSnapshot).map(OfferDatabaseMapper::toEntity).get();
        UserEntity ownerEntity = userRepository.findByUserId(offerSnapshot.owner().userId().getId()).orElseThrow(NoSuchElementException::new);
        offerEntity.setOwner(ownerEntity);

        offerRepository.save(offerEntity);
    }

    // UPDATE

    @Override
    public void updateOffer(Offer offer) {
        Optional<OfferEntity> offerEntity = offerRepository.findByOfferId(offer.getOfferId().getId());
        OfferEntity offerEntityFromDb = offerEntity.orElseThrow(NoSuchElementException::new);
        offerEntityFromDb.setTitle(offer.getTitle());
        offerEntityFromDb.setDescription(offer.getDescription());
        offerEntityFromDb.setAddress(AddressDatabaseMapper.toEntity(offer.getAddress()));
        offerEntityFromDb.setStatus(offer.getStatus());
        offerEntityFromDb.setCondition(offer.getCondition());
        offerEntityFromDb.setCategory(offer.getCategory());
        offerEntityFromDb.setPhotoId(offer.getPhotoId().getId());
        offerRepository.save(offerEntityFromDb);
    }

    // DELETE

    @Override
    @Transactional
    public void removeOffer(UUID offerId) {
        offerRepository.delete(offerRepository.findByOfferId(offerId).orElseThrow(NoSuchElementException::new));
    }



}
