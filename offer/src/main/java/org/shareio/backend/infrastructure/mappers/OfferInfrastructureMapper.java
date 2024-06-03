package org.shareio.backend.infrastructure.mappers;

import org.shareio.backend.Const;
import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferResponseDto;
import org.shareio.backend.core.usecases.util.DistanceCalculator;

import java.time.LocalDateTime;
import java.util.Objects;

public class OfferInfrastructureMapper {
    public static OfferResponseDto toDto(OfferSnapshot offerSnapshot, Location userLocation, Integer reviewCount, Double averageUserReviewValue) {
        LocalDateTime unreservationDate = null;
        String distance = null;
        if (offerSnapshot.status().equals(Status.RESERVED) && !Objects.isNull(offerSnapshot.reservationDate())) {
            unreservationDate = offerSnapshot.reservationDate().plus(Const.OFFER_RESERVATION_DURATION);
        }
        if(!Objects.isNull(userLocation)){
            distance = String.valueOf(DistanceCalculator.calculateDistance(offerSnapshot.address().getLocation(), userLocation));
        }
        if(Double.isNaN(averageUserReviewValue)){
            averageUserReviewValue = 0.0;
        }
        if (Objects.nonNull(offerSnapshot.reviewSnapshot())) {
            return new OfferResponseDto(
                    offerSnapshot.offerId().getId(),
                    offerSnapshot.creationDate(),
                    offerSnapshot.status().toString(),

                    offerSnapshot.address().getCity(),
                    offerSnapshot.address().getStreet(),
                    distance,
                    offerSnapshot.address().getHouseNumber(),
                    offerSnapshot.address().getLocation().getLatitude(),
                    offerSnapshot.address().getLocation().getLongitude(),

                    offerSnapshot.title(),
                    offerSnapshot.condition().polishName(),
                    offerSnapshot.category().polishName(),
                    offerSnapshot.description(),
                    offerSnapshot.photoId().getId(),

                    offerSnapshot.owner().userId().getId(),
                    offerSnapshot.owner().name(),
                    offerSnapshot.owner().surname(),
                    offerSnapshot.owner().photoId().getId(),
                    averageUserReviewValue,
                    reviewCount,

                    unreservationDate,
                    offerSnapshot.reviewSnapshot().reviewId().getId(),
                    offerSnapshot.reviewSnapshot().value(),
                    offerSnapshot.reviewSnapshot().date()
            );
        }
        else {
            return new OfferResponseDto(
                    offerSnapshot.offerId().getId(),
                    offerSnapshot.creationDate(),
                    offerSnapshot.status().toString(),

                    offerSnapshot.address().getCity(),
                    offerSnapshot.address().getStreet(),
                    distance,
                    offerSnapshot.address().getHouseNumber(),
                    offerSnapshot.address().getLocation().getLatitude(),
                    offerSnapshot.address().getLocation().getLongitude(),

                    offerSnapshot.title(),
                    offerSnapshot.condition().polishName(),
                    offerSnapshot.category().polishName(),
                    offerSnapshot.description(),
                    offerSnapshot.photoId().getId(),

                    offerSnapshot.owner().userId().getId(),
                    offerSnapshot.owner().name(),
                    offerSnapshot.owner().surname(),
                    offerSnapshot.owner().photoId().getId(),
                    averageUserReviewValue,
                    reviewCount,

                    unreservationDate,
                    null,
                    null,
                    null
            );
        }
    }

    private OfferInfrastructureMapper() {
        throw new IllegalStateException("Utility class");
    }
}
