package org.shareio.backend.infrastructure.dbadapter.repositories;

import org.shareio.backend.infrastructure.dbadapter.entities.AddressEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;


public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    Optional<AddressEntity> findByAddressId(UUID addressId);
}

