package org.shareio.backend.core.model;

import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.OfferId;
import org.shareio.backend.core.model.vo.PhotoId;
import org.shareio.backend.core.model.vo.Status;

import java.time.LocalDateTime;

public record OfferSnapshot(OfferId offerId, UserSnapshot owner, Address address,
                            LocalDateTime creationDate, Status status, UserSnapshot receiver,
                            LocalDateTime reservationDate, String title,
                            Condition condition,
                            String description,
                            PhotoId photoId) {
    public OfferSnapshot(Offer offer, UserSnapshot receiver) {
        this(offer.getOfferId(), offer.getOwner().toSnapshot(), offer.getAddress(), offer.getCreationDate(),
                offer.getStatus(), receiver, offer.getReservationDate(), offer.getTitle(),
                offer.getCondition(), offer.getDescription(), offer.getPhotoId());
    }
}
