package org.shareio.backend.infrastructure.dbadapter.repositories;

import jakarta.transaction.Transactional;
import org.shareio.backend.infrastructure.dbadapter.entities.AddressEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    Optional<AddressEntity> findByAddressId(UUID addressId);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE addresses CASCADE", nativeQuery = true)
    void truncateTable();
}

