package org.shareio.backend.core.usecases.port.out;

import java.util.UUID;

public interface RemoveReviewCommandInterface {
    void removeReview(UUID reviewId);
}
