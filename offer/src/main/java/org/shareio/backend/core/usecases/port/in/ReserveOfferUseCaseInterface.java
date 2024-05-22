package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.OfferReserveDto;

import java.util.UUID;

public interface ReserveOfferUseCaseInterface {
    UUID reserveOffer(OfferReserveDto offerReserveDto);
}
