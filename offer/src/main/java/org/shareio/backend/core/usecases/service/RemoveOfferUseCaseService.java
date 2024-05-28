package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.RemoveResponseDto;
import org.shareio.backend.core.usecases.port.in.RemoveOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetOfferDaoInterface;
import org.shareio.backend.core.usecases.port.out.RemoveOfferCommandInterface;
import org.shareio.backend.core.usecases.port.out.RemoveReviewCommandInterface;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Service
public class RemoveOfferUseCaseService implements RemoveOfferUseCaseInterface {
    GetOfferDaoInterface getOfferDaoInterface;
    RemoveOfferCommandInterface removeOfferCommandInterface;
    RemoveReviewCommandInterface removeReviewCommandInterface;

    @Override
    public RemoveResponseDto removeOffer(UUID offerId, RemoveResponseDto removeResponseDto) {
        OfferGetDto offerGetDto = getOfferDaoInterface.getOfferDto(offerId).orElseThrow(NoSuchElementException::new);
        if(Objects.nonNull(offerGetDto.reviewId())){
            removeReviewCommandInterface.removeReview(offerGetDto.reviewId());
            removeResponseDto.setDeletedReviewCount(removeResponseDto.getDeletedReviewCount()+1);
        }
        removeOfferCommandInterface.removeOffer(offerId);
        removeResponseDto.setDeletedOfferCount(removeResponseDto.getDeletedOfferCount()+1);
        return removeResponseDto;
    }
}
