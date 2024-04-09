package org.shareio.backend.infrastructure.dbadapter.mappers;

import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;

public class OfferMapper {
    public static OfferEntity toEntity(OfferSnapshot offerSnapshot) {
        return new OfferEntity(null, offerSnapshot.offerId().getId(),
                UserDatabaseMapper.toEntity(offerSnapshot.owner()),
                AddressDatabaseMapper.toEntity(offerSnapshot.address()),
                offerSnapshot.creationDate(),
                UserDatabaseMapper.toEntity(offerSnapshot.receiver()),
                offerSnapshot.reservationDate(),
                offerSnapshot.title(),
                offerSnapshot.description(),
                offerSnapshot.photoId().getId()
        );
    }
}
