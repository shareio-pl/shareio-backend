package org.shareio.backend.core.usecases.port.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.shareio.backend.core.usecases.port.enums.RemoveResponseFieldName;

@AllArgsConstructor
@Getter
@Setter
public class RemoveResponseDto {
    private Long deletedUserCount;
    private Long deletedSecurityCount;
    private Long deletedOfferCount;
    private Long deletedAddressCount;
    private Long deletedReviewCount;

    public RemoveResponseDto() {
        this.deletedUserCount = 0L;
        this.deletedSecurityCount = 0L;
        this.deletedOfferCount = 0L;
        this.deletedAddressCount = 0L;
        this.deletedReviewCount = 0L;
    }

    public void increment(RemoveResponseFieldName removeResponseFieldName){
        switch (removeResponseFieldName){
            case USERCOUNT -> this.deletedUserCount = this.deletedUserCount + 1;
            case ADDRESSCOUNT -> this.deletedAddressCount = this.deletedAddressCount + 1;
            case SECURITYCOUNT -> this.deletedSecurityCount = this.deletedSecurityCount + 1;
            case REVIEWCOUNT -> this.deletedReviewCount = this.deletedReviewCount + 1;
            case OFFERCOUNT -> this.deletedOfferCount = this.deletedOfferCount + 1;
        }
    }
}
