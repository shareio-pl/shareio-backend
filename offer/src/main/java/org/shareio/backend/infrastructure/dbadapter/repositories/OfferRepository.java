package org.shareio.backend.infrastructure.dbadapter.repositories;

import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface OfferRepository extends CrudRepository<OfferEntity, Long> {
    Optional<OfferEntity> findByOfferId(UUID offerId);
}
