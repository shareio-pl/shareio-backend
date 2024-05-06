package org.shareio.backend.core.usecases.port.out;

import org.shareio.backend.core.usecases.port.dto.OfferFullGetDto;

import java.util.Optional;
import java.util.UUID;

public interface GetOfferFullDaoInterface {
    Optional<OfferFullGetDto> getOfferFullDto(UUID id);

}
