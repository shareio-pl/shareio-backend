package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.exceptions.MultipleValidationException;

import java.util.List;
import java.util.UUID;

public interface GetOffersByUserAndStatusUseCaseInterface {
    List<UUID> getOfferResponseDtoListByUser(UUID id, Status status) throws MultipleValidationException;
}
