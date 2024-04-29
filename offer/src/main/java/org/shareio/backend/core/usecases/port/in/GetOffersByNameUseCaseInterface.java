package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.usecases.port.dto.OfferResponseDto;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.util.List;

public interface GetOffersByNameUseCaseInterface {
    List<OfferResponseDto> getOfferResponseDtoListByName(String name) throws MultipleValidationException;
}
