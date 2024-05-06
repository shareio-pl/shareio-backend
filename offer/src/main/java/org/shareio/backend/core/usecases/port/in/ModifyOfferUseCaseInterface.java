package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.OfferSaveDto;

public interface ModifyOfferUseCaseInterface {
    void modifyOffer(OfferSaveDto offerSaveDto);

}
