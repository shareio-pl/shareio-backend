package org.shareio.backend.core.usecases.port.out;

import org.shareio.backend.core.model.OfferSnapshot;

public interface UpdateOfferReserveOfferCommandInterface {
    void reserveOffer(OfferSnapshot offerSnapshot);
}
