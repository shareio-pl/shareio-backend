package org.shareio.backend.core.usecases.port.in;

import java.util.List;
import java.util.UUID;

public interface GetReservedOffersByRecieverUseCaseInterface {
    List<UUID> getReservedOffersByReciever(UUID recieverId);
}
