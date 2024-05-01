package org.shareio.backend.infrastructure.dbadapter.adapters;

import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.core.usecases.port.out.GetOffersByNameDaoInterface;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;
import org.shareio.backend.infrastructure.dbadapter.mappers.OfferDatabaseMapper;
import org.shareio.backend.infrastructure.dbadapter.repositories.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OfferAdapter implements GetOfferDaoInterface, GetOffersByNameDaoInterface {
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
    public List<OfferGetDto> getOffersByName(String name) {
        ArrayList<OfferEntity> offerList = (ArrayList<OfferEntity>) offerRepository.findAll();
        return offerList.stream().filter(offer -> Objects.equals(offer.getTitle(), name)).map(OfferDatabaseMapper::toDto).toList();
    }
}
