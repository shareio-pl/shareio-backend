package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.OfferSaveDto;
import org.shareio.backend.core.usecases.port.dto.OfferSaveResponseDto;
import org.shareio.backend.exceptions.LocationCalculationException;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.util.UUID;

public interface AddOfferUseCaseInterface {
    OfferSaveResponseDto addOffer(OfferSaveDto offerSaveDto, UUID photoId) throws MultipleValidationException, LocationCalculationException;
}
