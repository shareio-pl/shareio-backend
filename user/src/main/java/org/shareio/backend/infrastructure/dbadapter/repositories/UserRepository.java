package org.shareio.backend.infrastructure.dbadapter.repositories;

import jakarta.transaction.Transactional;
import org.shareio.backend.infrastructure.dbadapter.entities.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {
    List<UserEntity> findAll();
    Optional<UserEntity> findByUserId(UUID userId);

    Optional<UserEntity> findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE users CASCADE", nativeQuery = true)
    void truncateTable();
}
