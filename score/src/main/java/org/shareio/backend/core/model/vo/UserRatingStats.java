package org.shareio.backend.core.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRatingStats {
    private Integer reviewCount;
    private Float averageRating;
}
