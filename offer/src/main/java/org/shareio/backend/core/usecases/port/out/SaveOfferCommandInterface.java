package org.shareio.backend.core.usecases.port.out;

import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;

public interface SaveOfferCommandInterface {
    void saveOffer(OfferEntity offerEntity);
}
