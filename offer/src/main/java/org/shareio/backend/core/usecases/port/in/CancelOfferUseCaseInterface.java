package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.OfferEndDto;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.util.UUID;

public interface CancelOfferUseCaseInterface {
    UUID cancelOffer(OfferEndDto offerEndDto) throws MultipleValidationException;
}
