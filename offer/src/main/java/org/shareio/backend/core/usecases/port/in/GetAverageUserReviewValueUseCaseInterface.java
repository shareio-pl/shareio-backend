package org.shareio.backend.core.usecases.port.in;

import java.util.UUID;

public interface GetAverageUserReviewValueUseCaseInterface {
    Double getAverageUserReviewValue(UUID userId);
}
