package org.shareio.backend.core.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.OfferId;
import org.shareio.backend.core.model.vo.PhotoId;
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
    private User receiver;
    private LocalDateTime reservationDate;

    private String title;
    private Condition condition;
    private String description;
    private PhotoId photoId;

    public static Offer fromDto(OfferGetDto offerGetDto) {
        return new Offer(
                offerGetDto.offerId(),
                new User(offerGetDto.ownerId(), null, null, null, null, null, null),
                new Address(offerGetDto.addressId(), null, null, null, null, null, null, null, null),
                offerGetDto.creationDate(),
                new User(offerGetDto.receiverId(), null, null, null, null, null, null),
                offerGetDto.reservationDate(),
                offerGetDto.title(),
                offerGetDto.condition(),
                offerGetDto.description(),
                offerGetDto.photoId()
        );
    }

    public OfferSnapshot toSnapshot() {
        return new OfferSnapshot(this);
    }
}
