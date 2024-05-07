package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.exceptions.MultipleValidationException;

import java.util.List;
import java.util.UUID;

public interface GetOffersByUserUseCaseInterface {
    List<UUID> getOfferResponseDtoListByUser(UUID id) throws MultipleValidationException;
}
