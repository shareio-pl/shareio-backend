package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.OfferSaveDto;
import org.shareio.backend.core.usecases.port.dto.OfferSaveResponseDto;
import org.shareio.backend.exceptions.MultipleValidationException;

public interface AddOfferUseCaseInterface {
    OfferSaveResponseDto addOffer(OfferSaveDto offerSaveDto) throws MultipleValidationException;
}
