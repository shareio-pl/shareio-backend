package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.exceptions.MultipleValidationException;

import java.util.UUID;

public interface GetOfferOwnerIdUseCaseInterface {
    UUID getOfferOwnerId(UUID offerId) throws MultipleValidationException;
}
