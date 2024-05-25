package org.shareio.backend.core.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.shareio.backend.core.model.vo.ReviewId;
import org.shareio.backend.core.usecases.port.dto.ReviewGetDto;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Review {
    @Setter(AccessLevel.NONE)
    private ReviewId reviewId;
    private Double value; // 1-5 stars
    private LocalDateTime date;

    public ReviewSnapshot toSnapshot() {
        return new ReviewSnapshot(this);
    }

    public static Review fromDto(ReviewGetDto reviewGetDto){
        return new Review(
                new ReviewId(reviewGetDto.reviewId()),
                reviewGetDto.value(),
                reviewGetDto.date()
        );
    }
}
