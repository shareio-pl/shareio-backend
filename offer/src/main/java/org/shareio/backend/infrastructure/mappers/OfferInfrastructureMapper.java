package org.shareio.backend.infrastructure.mappers;

import org.shareio.backend.core.model.OfferSnapshot;
import org.shareio.backend.core.usecases.port.dto.OfferResponseDto;

import java.util.Objects;
import java.util.UUID;

public class OfferInfrastructureMapper {
    public static OfferResponseDto toDto(OfferSnapshot offerSnapshot) {
        UUID receiverId;
        if (Objects.isNull(offerSnapshot.receiver()) || Objects.isNull(offerSnapshot.receiver().userId())) {
            receiverId = null;
        } else {
            receiverId = offerSnapshot.receiver().userId().getId();
        }
        return new OfferResponseDto(
                offerSnapshot.offerId().getId(),
                offerSnapshot.owner().userId().getId(),
                offerSnapshot.address().getAddressId().getId(),
                offerSnapshot.creationDate(),
                receiverId,
                offerSnapshot.reservationDate(),
                offerSnapshot.title(),
                offerSnapshot.condition(),
                offerSnapshot.description(),
                offerSnapshot.photoId().toString()
        );
    }
}
