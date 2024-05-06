package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.usecases.port.dto.OfferSaveDto;
import org.shareio.backend.core.usecases.port.in.ModifyOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.SaveOfferCommandInterface;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ModifyOfferUseCaseService implements ModifyOfferUseCaseInterface {
    SaveOfferCommandInterface saveOfferCommandInterface;

    @Override
    public void modifyOffer(OfferSaveDto offerSaveDto) {
        //TODO: Create this method
    }
}
