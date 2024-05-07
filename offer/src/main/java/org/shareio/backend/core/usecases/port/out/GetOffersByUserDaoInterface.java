package org.shareio.backend.core.usecases.port.out;

import org.shareio.backend.core.usecases.port.dto.OfferGetDto;

import java.util.List;
import java.util.UUID;

public interface GetOffersByUserDaoInterface {
    List<OfferGetDto> getOffersByUser(UUID id);
}
