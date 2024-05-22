package org.shareio.backend.core.usecases.port.out;

import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;

import java.util.List;

public interface GetAllOffersByStatusDaoInterface {
    List<OfferGetDto> getAllOffersByStatus(Status status);
}
