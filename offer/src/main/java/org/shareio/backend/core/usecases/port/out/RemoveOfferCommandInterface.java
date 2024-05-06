package org.shareio.backend.core.usecases.port.out;

import java.util.UUID;

public interface RemoveOfferCommandInterface {
    void removeOffer(UUID offerId);
}
