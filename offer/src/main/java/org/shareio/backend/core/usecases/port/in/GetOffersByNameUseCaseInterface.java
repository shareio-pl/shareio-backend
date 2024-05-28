package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.exceptions.MultipleValidationException;

import java.util.List;
import java.util.UUID;

public interface GetOffersByNameUseCaseInterface {
    List<UUID> getOfferResponseDtoListByName(String name);
}
