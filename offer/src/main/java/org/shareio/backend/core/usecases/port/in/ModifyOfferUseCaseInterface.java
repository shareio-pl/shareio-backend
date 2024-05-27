package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.OfferModifyDto;
import org.shareio.backend.exceptions.LocationCalculationException;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.io.IOException;
import java.util.UUID;

public interface ModifyOfferUseCaseInterface {
    void modifyOffer(UUID offerId, OfferModifyDto offerModifyDto) throws LocationCalculationException, IOException, InterruptedException, MultipleValidationException;

}
