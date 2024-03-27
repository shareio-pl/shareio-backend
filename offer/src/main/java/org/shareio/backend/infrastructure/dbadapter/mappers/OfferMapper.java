package org.shareio.backend.infrastructure.dbadapter.mappers;

import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;

public class OfferMapper {
    public static OfferEntity toEntity(OfferSnapshot offerSnapshot) {
        return new OfferEntity(null, offerSnapshot.offerId().getId(),
                UserMapper.toEntity(offerSnapshot.owner()),
                AddressMapper.map(offerSnapshot.address()),
                offerSnapshot.creationDate(),
                UserMapper.toEntity(offerSnapshot.receiver()),
                offerSnapshot.reservationDate(),
                offerSnapshot.title(),
                offerSnapshot.description(),
                offerSnapshot.photoId().getId()
        );
    }
}
