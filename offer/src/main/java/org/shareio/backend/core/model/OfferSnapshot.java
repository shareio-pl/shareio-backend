package org.shareio.backend.core.model;

import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.OfferId;
import org.shareio.backend.core.model.vo.PhotoId;

import java.time.LocalDateTime;

public record OfferSnapshot(OfferId offerId, UserSnapshot owner, Address address, LocalDateTime creationDate,
                            UserSnapshot receiver, LocalDateTime reservationDate, String title, Condition condition,
                            String description,
                            PhotoId photoId) {
    public OfferSnapshot(Offer offer) {
        this(offer.getOfferId(), offer.getOwner().toSnapshot(), offer.getAddress(), offer.getCreationDate(),
                offer.getReceiver().toSnapshot(), offer.getReservationDate(), offer.getTitle(), offer.getCondition(),
                offer.getDescription(), offer.getPhotoId());
    }
}
