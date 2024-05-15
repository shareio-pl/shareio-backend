package org.shareio.backend.infrastructure.dbadapter.repositories;

import jakarta.transaction.Transactional;
import org.shareio.backend.infrastructure.dbadapter.entities.SecurityEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecurityRepository extends CrudRepository<SecurityEntity, Long> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE security CASCADE", nativeQuery = true)
    void truncateTable();
}
