package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.OfferResponseDto;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.util.UUID;

public interface GetOfferUseCaseInterface {
    OfferResponseDto getOfferResponseDto(UUID id, UUID userId, Integer reviewCount, Double averageUserReviewValue) throws MultipleValidationException;
}
