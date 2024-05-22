package org.shareio.backend.core.usecases.port.out;

import org.shareio.backend.core.model.ReviewSnapshot;

public interface SaveReviewCommandInterface {

    void saveReview(ReviewSnapshot reviewSnapshot);
}
