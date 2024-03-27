package org.shareio.backend.infrastructure.dbadapter.repositories;

import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUserId(UUID userId);
}
