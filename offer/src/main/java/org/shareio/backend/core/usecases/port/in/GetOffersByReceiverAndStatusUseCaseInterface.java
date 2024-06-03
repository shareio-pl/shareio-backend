package org.shareio.backend.core.usecases.port.in;

import org.shareio.backend.core.model.vo.Status;

import java.util.List;
import java.util.UUID;

public interface GetOffersByReceiverAndStatusUseCaseInterface {
    List<UUID> getReservedOffersByRecieverAndStatus(UUID recieverId, Status status);
}
