package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.EnvGetter;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.usecases.port.dto.RemoveResponseDto;
import org.shareio.backend.core.usecases.port.in.RemoveOffersForUserUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.shareio.backend.core.usecases.port.out.RemoveOfferCommandInterface;
import org.shareio.backend.exceptions.ImageException;
import org.shareio.backend.external.image.ImageStore;
import org.shareio.backend.external.image.ImageStoreInterface;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RemoveOffersForUserUseCaseService implements RemoveOffersForUserUseCaseInterface {
    GetAllOffersDaoInterface getAllOffersDaoInterface;
    RemoveOfferCommandInterface removeOfferCommandInterface;

    @Override
    public RemoveResponseDto removeOffersForUser(UUID userId, RemoveResponseDto removeResponseDto) {
        List<Offer> offerList = getAllOffersDaoInterface.getAllOffers().stream().map(Offer::fromDto).toList();
        offerList = offerList.stream().filter(offer -> Objects.equals(offer.getOwner().getUserId().getId(), userId)).toList();
        String imageServiceUrl = EnvGetter.getImage();
        ImageStoreInterface imageStore = new ImageStore(imageServiceUrl);
        for (Offer offer : offerList) {
            try {
                imageStore.DeleteImage(offer.getPhotoId().getId());
            } catch (NoSuchElementException | ImageException e) {
                System.out.println("Could not delete image " + offer.getPhotoId().getId());
                System.out.println(e.getMessage());
            }
            if (Objects.nonNull(offer.getReview())) {
                removeResponseDto.setDeletedReviewCount(removeResponseDto.getDeletedReviewCount() + 1);
            }
            removeOfferCommandInterface.removeOffer(offer.getOfferId().getId());
            removeResponseDto.setDeletedOfferCount(removeResponseDto.getDeletedOfferCount() + 1);
        }
        removeResponseDto.setDeletedAddressCount(removeResponseDto.getDeletedAddressCount() + offerList.size());
        return removeResponseDto;
    }
}
