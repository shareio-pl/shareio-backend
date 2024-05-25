package org.shareio.backend.core.usecases.port.in;

import java.util.UUID;

public interface GetOwnerReviewCountUseCaseInterface {
    Integer getUserReviewCount(UUID ownerId);

}
