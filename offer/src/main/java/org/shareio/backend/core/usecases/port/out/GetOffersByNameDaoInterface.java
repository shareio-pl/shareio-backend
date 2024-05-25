package org.shareio.backend.core.usecases.port.out;

import org.shareio.backend.core.usecases.port.dto.OfferGetDto;

import java.util.List;

public interface GetOffersByNameDaoInterface {
    List<OfferGetDto> getOffersByName(String name);
}
