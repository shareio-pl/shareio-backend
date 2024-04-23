package org.shareio.backend.core.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.shareio.backend.core.model.vo.*;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Offer {
    @Setter(AccessLevel.NONE)
    private OfferId offerId;
    private User owner;
    private Address address;
    private LocalDateTime creationDate;
    private Status status;
    private User receiver;
    private LocalDateTime reservationDate;

    private String title;
    private Condition condition;
    private String description;
    private PhotoId photoId;

    public static Offer fromDto(OfferGetDto offerGetDto) {
        User receiver = null;
        if (Status.valueOf(offerGetDto.status()).equals(Status.RESERVED)) {
            receiver = new User(null, null, null, null, null, null, null, null);
        }
        return new Offer(
                new OfferId(offerGetDto.offerId()),
                new User(new UserId(offerGetDto.ownerId()), null, offerGetDto.ownerName(), offerGetDto.ownerSurname(), null, new PhotoId(offerGetDto.ownerPhotoId()), null, null),
                new Address(null, null, null, offerGetDto.city(), offerGetDto.street(), offerGetDto.houseNumber(), null, null, new Location(offerGetDto.latitude(), offerGetDto.longitude())),
                offerGetDto.creationDate(),
                Status.valueOf(offerGetDto.status()),
                receiver,
                offerGetDto.reservationDate(),
                offerGetDto.title(),
                Condition.valueOf(offerGetDto.condition()),
                offerGetDto.description(),
                new PhotoId(offerGetDto.photoId())
        );
    }

    public OfferSnapshot toSnapshot() {
        UserSnapshot receiverSnapshot = null;
        if (this.status.equals(Status.RESERVED) && this.receiver != null) {
            receiverSnapshot = new UserSnapshot(this.receiver);
        }
        return new OfferSnapshot(this, receiverSnapshot);
    }
}
