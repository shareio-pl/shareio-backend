package org.shareio.backend.core.usecases.port.out;


import org.shareio.backend.core.usecases.port.dto.OfferGetDto;

import java.util.Optional;
import java.util.UUID;

public interface GetOfferDaoInterface {
    Optional<OfferGetDto> getOfferDto(UUID id);
}
