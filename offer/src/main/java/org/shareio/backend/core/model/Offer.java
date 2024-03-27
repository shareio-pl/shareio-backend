package org.shareio.backend.core.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.shareio.backend.core.model.vo.OfferId;
import org.shareio.backend.core.model.vo.PhotoId;

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
    private String description;
    private PhotoId photoId;

    public OfferSnapshot toSnapshot() {
        return new OfferSnapshot(this);
    }
}
