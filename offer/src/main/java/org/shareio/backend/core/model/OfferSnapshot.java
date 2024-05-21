package org.shareio.backend.core.model;

import org.shareio.backend.core.model.vo.*;

import java.time.LocalDateTime;

public record OfferSnapshot(OfferId offerId, UserSnapshot owner, Address address,
                            LocalDateTime creationDate, Status status, UserSnapshot receiver,
                            LocalDateTime reservationDate, String title,
                            Condition condition,
                            Category category,
                            String description,
                            PhotoId photoId,
                            ReviewSnapshot reviewSnapshot) {
    public OfferSnapshot(Offer offer, UserSnapshot receiver) {
        this(offer.getOfferId(), offer.getOwner().toSnapshot(), offer.getAddress(), offer.getCreationDate(),
                offer.getStatus(), receiver, offer.getReservationDate(), offer.getTitle(),
                offer.getCondition(), offer.getCategory(), offer.getDescription(), offer.getPhotoId(), offer.getReview().toSnapshot());
    }
}
