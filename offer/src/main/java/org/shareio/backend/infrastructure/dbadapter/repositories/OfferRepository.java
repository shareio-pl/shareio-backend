package org.shareio.backend.infrastructure.dbadapter.repositories;

import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OfferRepository extends CrudRepository<OfferEntity, Long> {
    Optional<OfferEntity> findByOfferId(UUID offerId);

    List<OfferEntity> findAllByStatus(Status status);
}
