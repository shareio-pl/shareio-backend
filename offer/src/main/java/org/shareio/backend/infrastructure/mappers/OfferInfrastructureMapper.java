package org.shareio.backend.infrastructure.mappers;

import org.shareio.backend.Const;
import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferResponseDto;

import java.time.LocalDateTime;
import java.util.Objects;

public class OfferInfrastructureMapper {
    public static OfferResponseDto toDto(OfferSnapshot offerSnapshot) {
        LocalDateTime unreservationDate = null;
        if (offerSnapshot.status().equals(Status.RESERVED) && !Objects.isNull(offerSnapshot.reservationDate())) {
            unreservationDate = offerSnapshot.reservationDate().plus(Const.offerReservationDuration);
        }
        return new OfferResponseDto(
                offerSnapshot.offerId().getId(),
                offerSnapshot.creationDate(),
                offerSnapshot.status().toString(),

                offerSnapshot.address().getCity(),
                offerSnapshot.address().getStreet(),
                "21,37 km", // TODO: distance calculation
                offerSnapshot.address().getLocation().getLatitude(),
                offerSnapshot.address().getLocation().getLongitude(),

                offerSnapshot.title(),
                offerSnapshot.condition().polishName(),
                offerSnapshot.description(),
                offerSnapshot.photoId().getId(),

                offerSnapshot.owner().userId().getId(),
                offerSnapshot.owner().name(),
                offerSnapshot.owner().surname(),
                offerSnapshot.owner().photoId().getId(),
                0.0, // TODO: offerSnapshot.owner().rating()
                0, // TODO: offerSnapshot.owner().reviewCount()

                unreservationDate
        );
    }
}
