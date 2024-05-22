package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.OfferModifyDto;
import org.shareio.backend.exceptions.LocationCalculationException;

import java.io.IOException;

public interface ModifyOfferUseCaseInterface {
    void modifyOffer(OfferModifyDto offerModifyDto) throws LocationCalculationException, IOException, InterruptedException;

}
