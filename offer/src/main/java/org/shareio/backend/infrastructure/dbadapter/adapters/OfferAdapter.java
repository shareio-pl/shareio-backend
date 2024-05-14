package org.shareio.backend.infrastructure.dbadapter.adapters;

import jakarta.transaction.Transactional;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.usecases.port.dto.OfferFullGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.out.*;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;
import org.shareio.backend.infrastructure.dbadapter.mappers.AddressDatabaseMapper;
import org.shareio.backend.infrastructure.dbadapter.mappers.OfferDatabaseMapper;
import org.shareio.backend.infrastructure.dbadapter.repositories.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OfferAdapter implements GetOfferDaoInterface, GetAllOffersDaoInterface, GetOffersByNameDaoInterface, RemoveOfferCommandInterface, SaveOfferCommandInterface, GetOfferFullDaoInterface, UpdateOfferDaoInterface, GetOffersByUserDaoInterface {
    final OfferRepository offerRepository;

    public OfferAdapter(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }


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
    @Transactional
    public void removeOffer(UUID offerId) {
        offerRepository.delete(offerRepository.findByOfferId(offerId).orElseThrow(NoSuchElementException::new));
    }

    @Override
    public void saveOffer(OfferEntity offerEntity) {

        offerRepository.save(offerEntity);
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
    public void updateOffer(Offer offer) {
        Optional<OfferEntity> offerEntity = offerRepository.findByOfferId(offer.getOfferId().getId());
        OfferEntity offerEntity1 = offerEntity.orElseThrow(NoSuchElementException::new);
        offerEntity1.setTitle(offer.getTitle());
        offerEntity1.setDescription(offer.getDescription());
        offerEntity1.setAddress(AddressDatabaseMapper.toEntity(offer.getAddress()));
        offerEntity1.setStatus(offer.getStatus());
        offerEntity1.setCondition(offer.getCondition());
        offerEntity1.setCategory(offer.getCategory());
        offerEntity1.setPhotoId(offer.getPhotoId().getId());
        offerRepository.save(offerEntity1);
    }

    @Override
    public List<OfferGetDto> getOffersByUser(UUID id) {
        ArrayList<OfferEntity> offerList = (ArrayList<OfferEntity>) offerRepository.findAll();
        return offerList.stream().filter(offer -> Objects.equals(offer.getOwner().getUserId(), id)).map(OfferDatabaseMapper::toDto).toList();
    }
}
