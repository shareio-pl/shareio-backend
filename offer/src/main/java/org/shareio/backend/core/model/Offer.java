package org.shareio.backend.core.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.shareio.backend.Const;
import org.shareio.backend.core.model.vo.*;
import org.shareio.backend.core.usecases.port.dto.OfferFullGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferSaveDto;

import java.time.LocalDateTime;
import java.util.UUID;

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
    private Category category;
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
                Category.valueOf(offerGetDto.category()),
                offerGetDto.description(),
                new PhotoId(offerGetDto.photoId())
        );
    }

    public static Offer fromDto(OfferFullGetDto offerFullGetDto) {
        User receiver = null;
        if (Status.valueOf(offerFullGetDto.status()).equals(Status.RESERVED)) {
            receiver = new User(null, null, null, null, null, null, null, null);
        }
        return new Offer(
                new OfferId(offerFullGetDto.offerId()),
                new User(new UserId(offerFullGetDto.ownerId()), null, offerFullGetDto.ownerName(), offerFullGetDto.ownerSurname(), null, new PhotoId(offerFullGetDto.ownerPhotoId()), null, null),
                new Address(new AddressId(offerFullGetDto.addressId()), offerFullGetDto.country(), offerFullGetDto.region(), offerFullGetDto.city(), offerFullGetDto.street(), offerFullGetDto.houseNumber(), null, null, new Location(offerFullGetDto.latitude(), offerFullGetDto.longitude())),
                offerFullGetDto.creationDate(),
                Status.valueOf(offerFullGetDto.status()),
                receiver,
                offerFullGetDto.reservationDate(),
                offerFullGetDto.title(),
                Condition.valueOf(offerFullGetDto.condition()),
                Category.valueOf(offerFullGetDto.category()),
                offerFullGetDto.description(),
                new PhotoId(offerFullGetDto.photoId())
        );
    }

    public static Offer fromDto(OfferSaveDto offerSaveDto) {
        return new Offer(
                new OfferId(UUID.randomUUID()),
                null,
                new Address(new AddressId(UUID.randomUUID()), offerSaveDto.addressSaveDto().country(), offerSaveDto.addressSaveDto().region(), offerSaveDto.addressSaveDto().city(), offerSaveDto.addressSaveDto().street(), offerSaveDto.addressSaveDto().houseNumber(), offerSaveDto.addressSaveDto().flatNumber(), offerSaveDto.addressSaveDto().postCode(), new Location(Const.defaultAddressCenterLat, Const.defaultAddressCenterLon)),
                offerSaveDto.creationDate(),
                Status.CREATED,
                null,
                null,
                offerSaveDto.title(),
                Condition.valueOf(offerSaveDto.condition()),
                Category.valueOf(offerSaveDto.category()),
                offerSaveDto.description(),
                new PhotoId(UUID.randomUUID())
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
