package org.shareio.backend.infrastructure.dbadapter.repositories;

import org.shareio.backend.infrastructure.dbadapter.entities.ReviewEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends CrudRepository<ReviewEntity, Long> {
    Optional<ReviewEntity> findByReviewId(UUID reviewId);
}

