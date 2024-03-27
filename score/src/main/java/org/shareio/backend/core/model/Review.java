package org.shareio.backend.core.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.shareio.backend.core.model.vo.ReviewId;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Review {
    @Setter(AccessLevel.NONE)
    private ReviewId reviewId;
    private Offer offer;
    private Integer value; // 1-5 stars
    private LocalDateTime date;

    public ReviewSnapshot toSnapshot() {
        return new ReviewSnapshot(this);
    }
}
