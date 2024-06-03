package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.RemoveResponseDto;

import java.util.UUID;

public interface RemoveOfferUseCaseInterface {
    RemoveResponseDto removeOffer(UUID offerId, RemoveResponseDto removeResponseDto);
}
