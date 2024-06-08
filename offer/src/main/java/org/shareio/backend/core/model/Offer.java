package org.shareio.backend.core.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.shareio.backend.Const;
import org.shareio.backend.core.model.vo.*;
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
    private Review review;

    public static Offer fromDto(OfferGetDto offerGetDto) {
        User receiver = null;
        Review review = null;
        if (Status.valueOf(offerGetDto.status()).equals(Status.RESERVED)
                || Status.valueOf(offerGetDto.status()).equals(Status.FINISHED)
        ) {
            receiver = new User(new UserId(offerGetDto.recieverId()), null, null, null, null, null, null, null);
        }
        if (offerGetDto.reviewId() != null) {
            review = new Review(new ReviewId(offerGetDto.reviewId()), offerGetDto.revievValue(), offerGetDto.reviewDate());

        }
        return new Offer(
                new OfferId(offerGetDto.offerId()),
                new User(new UserId(offerGetDto.ownerId()), null, offerGetDto.ownerName(), offerGetDto.ownerSurname(), null, new PhotoId(offerGetDto.ownerPhotoId()), null, null),
                new Address(new AddressId(offerGetDto.addressId()), offerGetDto.country(), offerGetDto.region(), offerGetDto.city(), offerGetDto.street(), offerGetDto.houseNumber(), offerGetDto.flatNumber(), offerGetDto.postCode(), new Location(offerGetDto.latitude(), offerGetDto.longitude())),
                offerGetDto.creationDate(),
                Status.valueOf(offerGetDto.status()),
                receiver,
                offerGetDto.reservationDate(),
                offerGetDto.title(),
                Condition.valueOf(offerGetDto.condition()),
                Category.valueOf(offerGetDto.category()),
                offerGetDto.description(),
                new PhotoId(offerGetDto.photoId()),
                review
        );
    }

    public static Offer fromDto(OfferSaveDto offerSaveDto) {
        return new Offer(
                new OfferId(UUID.randomUUID()),
                null,
                new Address(new AddressId(UUID.randomUUID()), offerSaveDto.country(), offerSaveDto.region(), offerSaveDto.city(), offerSaveDto.street(), offerSaveDto.houseNumber(), null, null, new Location(Const.DEFAULT_ADDRESS_CENTER_LAT, Const.DEFAULT_ADDRESS_CENTER_LON)),
                LocalDateTime.now(),
                Status.CREATED,
                null,
                null,
                offerSaveDto.title(),
                Condition.valueOf(offerSaveDto.condition()),
                Category.valueOf(offerSaveDto.category()),
                offerSaveDto.description(),
                new PhotoId(UUID.randomUUID()),
                null
        );
    }

    public OfferSnapshot toSnapshot() {
        UserSnapshot receiverSnapshot = null;
        ReviewSnapshot reviewSnapshot = null;
        if (this.status.equals(Status.RESERVED) && this.receiver != null) {
            receiverSnapshot = new UserSnapshot(this.receiver);
        }
        if (this.review != null) {
            reviewSnapshot = new ReviewSnapshot(this.review);
        }
        return new OfferSnapshot(this, receiverSnapshot, reviewSnapshot);
    }
}
