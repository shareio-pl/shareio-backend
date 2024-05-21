package org.shareio.backend.infrastructure.dbadapter.mappers;

import org.shareio.backend.core.model.OfferSnapshot;
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
                offerEntity.getCategory().toString(),
                offerEntity.getDescription(),
                offerEntity.getPhotoId(),

                offerEntity.getOwner().getUserId(),
                offerEntity.getOwner().getName(),
                offerEntity.getOwner().getSurname(),
                offerEntity.getOwner().getPhotoId(),

                0.0,// TODO: offerEntity.getOwner().getRating()
                0, // TODO: offerEntity.getOwner().getReviewCount()

                offerEntity.getReservationDate(),
                offerEntity.getReview().getReviewId(),
                offerEntity.getReview().getReviewValue(),
                offerEntity.getReview().getReviewDate()
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
                offerSnapshot.category(),
                offerSnapshot.description(),
                offerSnapshot.photoId().getId(),
                null
        );
    }
}
