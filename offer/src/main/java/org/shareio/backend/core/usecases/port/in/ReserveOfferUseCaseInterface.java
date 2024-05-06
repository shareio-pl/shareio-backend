package org.shareio.backend.core.usecases.port.in;

import java.util.UUID;

public interface ReserveOfferUseCaseInterface {
    UUID reserveOffer(UUID offerId, UUID userId);
}
