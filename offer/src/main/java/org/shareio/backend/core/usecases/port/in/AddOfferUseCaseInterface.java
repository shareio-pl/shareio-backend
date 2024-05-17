package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.OfferSaveDto;
import org.shareio.backend.core.usecases.port.dto.OfferSaveResponseDto;
import org.shareio.backend.exceptions.LocationCalculationException;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.io.IOException;

public interface AddOfferUseCaseInterface {
    OfferSaveResponseDto addOffer(OfferSaveDto offerSaveDto) throws MultipleValidationException, LocationCalculationException, IOException, InterruptedException;
}
