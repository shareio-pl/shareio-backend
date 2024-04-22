package org.shareio.backend.infrastructure.dbadapter.mappers;

import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.model.vo.AddressId;
import org.shareio.backend.core.model.vo.OfferId;
import org.shareio.backend.core.model.vo.PhotoId;
import org.shareio.backend.core.model.vo.UserId;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;

import java.util.Objects;

public class OfferDatabaseMapper {
    public static OfferGetDto toDto(final OfferEntity offerEntity) {
        UserId receiverId;
        if (Objects.isNull(offerEntity.getReceiver())) {
            receiverId = null;
        } else {
            receiverId = new UserId(offerEntity.getReceiver().getUserId());
        }
        return new OfferGetDto(
                new OfferId(offerEntity.getOfferId()),
                new UserId(offerEntity.getOwner().getUserId()),
                new AddressId(offerEntity.getAddress().getAddressId()),
                offerEntity.getCreationDate(),
                receiverId,
                offerEntity.getReservationDate(),
                offerEntity.getTitle(),
                offerEntity.getCondition(),
                offerEntity.getDescription(),
                new PhotoId(offerEntity.getPhotoId())
        );
    }

    public static OfferEntity toEntity(OfferSnapshot offerSnapshot) {
        return new OfferEntity(null, offerSnapshot.offerId().getId(),
                UserDatabaseMapper.toEntity(offerSnapshot.owner()),
                AddressDatabaseMapper.toEntity(offerSnapshot.address()),
                offerSnapshot.creationDate(),
                UserDatabaseMapper.toEntity(offerSnapshot.receiver()),
                offerSnapshot.reservationDate(),
                offerSnapshot.title(),
                offerSnapshot.condition(),
                offerSnapshot.description(),
                offerSnapshot.photoId().getId()
        );
    }
}
