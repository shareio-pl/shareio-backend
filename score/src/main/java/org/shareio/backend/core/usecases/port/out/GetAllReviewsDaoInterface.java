package org.shareio.backend.core.usecases.port.out;

import org.shareio.backend.core.usecases.port.dto.ReviewGetDto;

import java.util.List;

public interface GetAllReviewsDaoInterface {
    List<ReviewGetDto> getAllReviews();
}
