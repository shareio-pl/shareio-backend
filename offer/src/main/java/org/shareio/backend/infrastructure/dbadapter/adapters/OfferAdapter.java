package org.shareio.backend.infrastructure.dbadapter.adapters;

import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.out.*;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.ReviewEntity;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
import org.shareio.backend.infrastructure.dbadapter.mappers.OfferDatabaseMapper;
import org.shareio.backend.infrastructure.dbadapter.mappers.ReviewMapper;
import org.shareio.backend.infrastructure.dbadapter.repositories.OfferRepository;
import org.shareio.backend.infrastructure.dbadapter.repositories.ReviewRepository;
import org.shareio.backend.infrastructure.dbadapter.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OfferAdapter implements GetOfferDaoInterface, GetAllOffersDaoInterface, GetOffersByNameDaoInterface,
        RemoveOfferCommandInterface, SaveOfferCommandInterface, UpdateOfferCancelOfferCommandInterface, UpdateOfferChangeMetadataCommandInterface, GetOffersByUserDaoInterface,
        UpdateOfferSaveReviewCommandInterface, UpdateOfferFinishOfferCommandInterface, UpdateOfferReserveOfferCommandInterface, UpdateOfferDereserveOfferCommandInterface {
    final OfferRepository offerRepository;
    final UserRepository userRepository;
    final ReviewRepository reviewRepository;

    public OfferAdapter(OfferRepository offerRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
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

        OfferEntity offerEntity = Optional.of(offerSnapshot).map(OfferDatabaseMapper::toEntity).orElseThrow(NoSuchElementException::new);
        UserEntity ownerEntity = userRepository.findByUserId(offerSnapshot.owner().userId().getId()).orElseThrow(NoSuchElementException::new);
        offerEntity.setOwner(ownerEntity);

        offerRepository.save(offerEntity);
    }

    // UPDATE

    @Override
    public void updateOfferMetadata(OfferSnapshot offerSnapshot) {
        Optional<OfferEntity> offerEntity = offerRepository.findByOfferId(offerSnapshot.offerId().getId());
        OfferEntity offerEntityFromDb = offerEntity.orElseThrow(NoSuchElementException::new);
        offerEntityFromDb.setTitle(offerSnapshot.title());
        offerEntityFromDb.setDescription(offerSnapshot.description());
        offerEntityFromDb.getAddress().setCountry(offerSnapshot.address().getCountry());
        offerEntityFromDb.getAddress().setRegion(offerSnapshot.address().getRegion());
        offerEntityFromDb.getAddress().setCity(offerSnapshot.address().getCity());
        offerEntityFromDb.getAddress().setStreet(offerSnapshot.address().getStreet());
        offerEntityFromDb.getAddress().setHouseNumber(offerSnapshot.address().getHouseNumber());
        offerEntityFromDb.getAddress().setFlatNumber(offerSnapshot.address().getFlatNumber());
        offerEntityFromDb.getAddress().setPostCode(offerSnapshot.address().getPostCode());
        offerEntityFromDb.getAddress().setLatitude(offerSnapshot.address().getLocation().getLatitude());
        offerEntityFromDb.getAddress().setLongitude(offerSnapshot.address().getLocation().getLongitude());
        offerEntityFromDb.setCondition(offerSnapshot.condition());
        offerEntityFromDb.setCategory(offerSnapshot.category());
        offerRepository.save(offerEntityFromDb);
    }

    @Override
    public void updateOfferAddReview(OfferSnapshot offerSnapshot) {
        Optional<OfferEntity> offerEntity = offerRepository.findByOfferId(offerSnapshot.offerId().getId());
        OfferEntity offerEntityFromDb = offerEntity.orElseThrow(NoSuchElementException::new);
        offerEntityFromDb.setReview(Optional.of(offerSnapshot.reviewSnapshot()).map(ReviewMapper::toEntity).orElseThrow(NoSuchElementException::new));
        offerRepository.save(offerEntityFromDb);
    }

    @Override
    public void reserveOffer(OfferSnapshot offerSnapshot) {
        Optional<OfferEntity> offerEntity = offerRepository.findByOfferId(offerSnapshot.offerId().getId());
        OfferEntity offerEntityFromDb = offerEntity.orElseThrow(NoSuchElementException::new);
        UserEntity receiverEntityFromDb = userRepository.findByUserId(offerSnapshot.receiver().userId().getId()).orElseThrow(NoSuchElementException::new);
        offerEntityFromDb.setReceiver(receiverEntityFromDb);
        offerEntityFromDb.setStatus(offerSnapshot.status());
        offerEntityFromDb.setReservationDate(offerSnapshot.reservationDate());
        offerRepository.save(offerEntityFromDb);
    }

    @Override
    public void dereserveOffer(OfferSnapshot offerSnapshot) {
        Optional<OfferEntity> offerEntity = offerRepository.findByOfferId(offerSnapshot.offerId().getId());
        OfferEntity offerEntityFromDb = offerEntity.orElseThrow(NoSuchElementException::new);
        offerEntityFromDb.setReceiver(null); // offerSnapshot.receiver()
        offerEntityFromDb.setStatus(offerSnapshot.status());
        offerEntityFromDb.setReservationDate(offerSnapshot.reservationDate());
        offerRepository.save(offerEntityFromDb);
    }

    @Override
    public void finishOffer(OfferSnapshot offerSnapshot) {
        Optional<OfferEntity> offerEntity = offerRepository.findByOfferId(offerSnapshot.offerId().getId());
        OfferEntity offerEntityFromDb = offerEntity.orElseThrow(NoSuchElementException::new);
        offerEntityFromDb.setStatus(offerSnapshot.status());
        offerRepository.save(offerEntityFromDb);
    }

    @Override
    public void cancelOffer(OfferSnapshot offerSnapshot) {
        Optional<OfferEntity> offerEntity = offerRepository.findByOfferId(offerSnapshot.offerId().getId());
        OfferEntity offerEntityFromDb = offerEntity.orElseThrow(NoSuchElementException::new);
        offerEntityFromDb.setReceiver(null); // offerSnapshot.receiver()
        offerEntityFromDb.setReservationDate(offerSnapshot.reservationDate());
        offerEntityFromDb.setStatus(offerSnapshot.status());
        offerRepository.save(offerEntityFromDb);
    }

    // DELETE

    @Override
    public void removeOffer(UUID offerId) {
        Optional<OfferEntity> offerEntity = offerRepository.findByOfferId(offerId);
        ReviewEntity reviewEntity;
        OfferEntity offerEntityFromDb = offerEntity.orElseThrow(NoSuchElementException::new);
        offerEntityFromDb.setStatus(Status.CANCELED);
        if (Objects.nonNull(offerEntityFromDb.getReview())) {
            reviewEntity = offerEntityFromDb.getReview();
            offerEntityFromDb.setReview(null);
            reviewRepository.delete(reviewEntity);
        } else {
            offerEntityFromDb.setReview(null);
        }

        offerRepository.save(offerEntityFromDb);
    }

}
