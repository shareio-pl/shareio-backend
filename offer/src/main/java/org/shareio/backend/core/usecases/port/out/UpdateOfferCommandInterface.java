package org.shareio.backend.core.usecases.port.out;

import org.shareio.backend.core.model.Offer;

public interface UpdateOfferCommandInterface {
    void updateOffer(Offer offer);
}
