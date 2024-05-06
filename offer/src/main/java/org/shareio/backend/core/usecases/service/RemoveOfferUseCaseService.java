package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.usecases.port.dto.RemoveResponseDto;
import org.shareio.backend.core.usecases.port.in.RemoveOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.RemoveOfferCommandInterface;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class RemoveOfferUseCaseService implements RemoveOfferUseCaseInterface {

    RemoveOfferCommandInterface removeOfferCommandInterface;

    @Override
    public RemoveResponseDto removeOffer(UUID offerId, RemoveResponseDto removeResponseDto) {

        removeOfferCommandInterface.removeOffer(offerId);
        removeResponseDto.setDeletedOfferCount(removeResponseDto.getDeletedOfferCount()+1);
        return removeResponseDto;
    }
}
