package org.shareio.backend.infrastructure.dbadapter.adapters;

import jakarta.transaction.Transactional;
import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.out.*;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
import org.shareio.backend.infrastructure.dbadapter.mappers.AddressDatabaseMapper;
import org.shareio.backend.infrastructure.dbadapter.mappers.OfferDatabaseMapper;
import org.shareio.backend.infrastructure.dbadapter.mappers.ReviewMapper;
import org.shareio.backend.infrastructure.dbadapter.repositories.OfferRepository;
import org.shareio.backend.infrastructure.dbadapter.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OfferAdapter implements GetOfferDaoInterface, GetAllOffersDaoInterface, GetOffersByNameDaoInterface,
        RemoveOfferCommandInterface, SaveOfferCommandInterface, UpdateOfferCommandInterface, GetOffersByUserDaoInterface,
        UpdateOfferSaveReviewCommandInterface, UpdateOfferReserveOfferCommandInterface {
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
    public void updateOffer(OfferSnapshot offerSnapshot) {
        Optional<OfferEntity> offerEntity = offerRepository.findByOfferId(offerSnapshot.offerId().getId());
        OfferEntity offerEntityFromDb = offerEntity.orElseThrow(NoSuchElementException::new);
        offerEntityFromDb.setTitle(offerSnapshot.title());
        offerEntityFromDb.setDescription(offerSnapshot.description());
        offerEntityFromDb.setAddress(AddressDatabaseMapper.toEntity(offerSnapshot.address()));
        offerEntityFromDb.setCondition(offerSnapshot.condition());
        offerEntityFromDb.setCategory(offerSnapshot.category());
        offerRepository.save(offerEntityFromDb);
    }

    @Override
    public void updateOfferAddReview(OfferSnapshot offerSnapshot) {
        Optional<OfferEntity> offerEntity = offerRepository.findByOfferId(offerSnapshot.offerId().getId());
        OfferEntity offerEntityFromDb = offerEntity.orElseThrow(NoSuchElementException::new);
        offerEntityFromDb.setReview(Optional.of(offerSnapshot.reviewSnapshot()).map(ReviewMapper::toEntity).get());
        offerRepository.save(offerEntityFromDb);
    }

    @Override
    public void reserveOffer(OfferSnapshot offerSnapshot){
        Optional<OfferEntity> offerEntity = offerRepository.findByOfferId(offerSnapshot.offerId().getId());
        OfferEntity offerEntityFromDb = offerEntity.orElseThrow(NoSuchElementException::new);
        UserEntity recieverEntityFromDb = userRepository.findByUserId(offerSnapshot.receiver().userId().getId()).orElseThrow(NoSuchElementException::new);
        offerEntityFromDb.setReceiver(recieverEntityFromDb);
        offerEntityFromDb.setStatus(offerSnapshot.status());
        offerEntityFromDb.setReservationDate(offerSnapshot.reservationDate());
        offerRepository.save(offerEntityFromDb);
    }

    // DELETE

    @Override
    @Transactional
    public void removeOffer(UUID offerId) {
        offerRepository.delete(offerRepository.findByOfferId(offerId).orElseThrow(NoSuchElementException::new));
    }



}
