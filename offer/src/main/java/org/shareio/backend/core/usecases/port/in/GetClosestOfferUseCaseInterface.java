package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.model.vo.Location;

import java.util.NoSuchElementException;
import java.util.UUID;

public interface GetClosestOfferUseCaseInterface {
    UUID getOfferResponseDto(Location location, UUID userId) throws NoSuchElementException;
}
