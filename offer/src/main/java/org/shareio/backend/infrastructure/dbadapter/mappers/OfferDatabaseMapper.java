package org.shareio.backend.infrastructure.dbadapter.mappers;

import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.usecases.port.dto.OfferFullGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;

public class OfferDatabaseMapper {
    public static OfferGetDto toDto(final OfferEntity offerEntity) {
        return new OfferGetDto(
                offerEntity.getOfferId(),
                offerEntity.getCreationDate(),
                offerEntity.getStatus().toString(),

                offerEntity.getAddress().getCity(),
                offerEntity.getAddress().getStreet(),
                offerEntity.getAddress().getHouseNumber(),
                offerEntity.getAddress().getLatitude(),
                offerEntity.getAddress().getLongitude(),

                offerEntity.getTitle(),
                offerEntity.getCondition().toString(),
                offerEntity.getDescription(),
                offerEntity.getPhotoId(),

                offerEntity.getOwner().getUserId(),
                offerEntity.getOwner().getName(),
                offerEntity.getOwner().getSurname(),
                offerEntity.getOwner().getPhotoId(),

                0.0,// TODO: offerEntity.getOwner().getRating()
                0, // TODO: offerEntity.getOwner().getReviewCount()

                offerEntity.getReservationDate()


        );
    }

    public static OfferFullGetDto toFullDto(final OfferEntity offerEntity) {
        return new OfferFullGetDto(
                offerEntity.getOfferId(),
                offerEntity.getCreationDate(),
                offerEntity.getStatus().toString(),

                offerEntity.getAddress().getAddressId(),
                offerEntity.getAddress().getCountry(),
                offerEntity.getAddress().getRegion(),
                offerEntity.getAddress().getCity(),
                offerEntity.getAddress().getStreet(),
                offerEntity.getAddress().getHouseNumber(),
                offerEntity.getAddress().getLatitude(),
                offerEntity.getAddress().getLongitude(),

                offerEntity.getTitle(),
                offerEntity.getCondition().toString(),
                offerEntity.getDescription(),
                offerEntity.getPhotoId(),

                offerEntity.getOwner().getUserId(),
                offerEntity.getOwner().getName(),
                offerEntity.getOwner().getSurname(),
                offerEntity.getOwner().getPhotoId(),

                0.0,// TODO: offerEntity.getOwner().getRating()
                0, // TODO: offerEntity.getOwner().getReviewCount()

                offerEntity.getReservationDate()


        );
    }

    public static OfferEntity toEntity(OfferSnapshot offerSnapshot) {
        return new OfferEntity(null, offerSnapshot.offerId().getId(),
                null,
                AddressDatabaseMapper.toEntity(offerSnapshot.address()),
                offerSnapshot.creationDate(),
                offerSnapshot.status(),
                null,
                offerSnapshot.reservationDate(),
                offerSnapshot.title(),
                offerSnapshot.condition(),
                offerSnapshot.description(),
                offerSnapshot.photoId().getId()
        );
    }
}
