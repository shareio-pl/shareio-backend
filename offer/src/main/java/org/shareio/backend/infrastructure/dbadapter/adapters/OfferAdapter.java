package org.shareio.backend.infrastructure.dbadapter.adapters;

import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;
import org.shareio.backend.infrastructure.dbadapter.mappers.OfferDatabaseMapper;
import org.shareio.backend.infrastructure.dbadapter.repositories.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class OfferAdapter implements GetOfferDaoInterface {
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
}
