package org.shareio.backend.infrastructure.dbadapter.mappers;

import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;

import java.util.Objects;
import java.util.UUID;

public class OfferDatabaseMapper {
    public static OfferGetDto toDto(final OfferEntity offerEntity) {
        UUID recieverId = null;
        if(Objects.nonNull(offerEntity.getReceiver())){
            recieverId = offerEntity.getReceiver().getUserId();
        }
        if(offerEntity.getReview() != null){
            return new OfferGetDto(
                    offerEntity.getOfferId(),
                    offerEntity.getCreationDate(),
                    offerEntity.getStatus().toString(),

                    offerEntity.getAddress().getAddressId(),
                    offerEntity.getAddress().getCountry(),
                    offerEntity.getAddress().getRegion(),
                    offerEntity.getAddress().getCity(),
                    offerEntity.getAddress().getStreet(),
                    offerEntity.getAddress().getHouseNumber(),
                    offerEntity.getAddress().getFlatNumber(),
                    offerEntity.getAddress().getPostCode(),
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

                    recieverId,

                    offerEntity.getReservationDate(),
                    offerEntity.getReview().getReviewId(),
                    offerEntity.getReview().getReviewValue(),
                    offerEntity.getReview().getReviewDate()
            );
        }
        else {
            return new OfferGetDto(
                    offerEntity.getOfferId(),
                    offerEntity.getCreationDate(),
                    offerEntity.getStatus().toString(),

                    offerEntity.getAddress().getAddressId(),
                    offerEntity.getAddress().getCountry(),
                    offerEntity.getAddress().getRegion(),
                    offerEntity.getAddress().getCity(),
                    offerEntity.getAddress().getStreet(),
                    offerEntity.getAddress().getHouseNumber(),
                    offerEntity.getAddress().getFlatNumber(),
                    offerEntity.getAddress().getPostCode(),
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

                    recieverId,

                    offerEntity.getReservationDate(),
                    null,
                    null,
                    null
            );
        }

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
    private OfferDatabaseMapper() {
        throw new IllegalArgumentException("Utility class");
    }
}
