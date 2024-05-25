package org.shareio.backend.core.usecases.port.in;

import java.util.UUID;

public interface GetOwnerReviewCountUseCaseInterface {
    Long getUserReviewCount(UUID ownerId);

}
