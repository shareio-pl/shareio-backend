package org.shareio.backend.core.usecases.port.out;

import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.infrastructure.dbadapter.entities.OfferEntity;

import java.util.List;

public interface GetOffersByNameDaoInterface {
    List<OfferGetDto> getOffersByName(String name);
}
