package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.util.DistanceCalculator;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.OfferValidator;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.usecases.port.dto.*;
import org.shareio.backend.core.usecases.port.in.GetClosestOfferUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.shareio.backend.core.usecases.port.out.GetLocationDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class GetClosestOfferUseCaseService implements GetClosestOfferUseCaseInterface {
    GetAllOffersDaoInterface getAllOffersDaoInterface;
    GetLocationDaoInterface getLocationDaoInterface;

    @Override
    public UUID getOfferResponseDto(Location location, UUID userId) throws NoSuchElementException {
        List<OfferGetDto> allOfferList = getAllOffersDaoInterface.getAllOffers();
        AtomicReference<UUID> closestOfferId = new AtomicReference<>();
        AtomicReference<Double> distance = new AtomicReference<>(Double.MAX_VALUE);
        allOfferList = allOfferList.stream()
                .filter(offer -> {
            try {
                OfferValidator.validateOffer(offer);
                return true;
            } catch (MultipleValidationException e) {
                return false;
            }
        })

                .toList();
        if (allOfferList.isEmpty()) {
            throw new NoSuchElementException("No valid offers found!");
        }
        List<Offer> offerList = allOfferList.stream().map(Offer::fromDto).toList();
        offerList = offerList
                .stream()
                .filter(offer-> !Objects.equals(offer.getOwner().getUserId().getId(), userId))
                .filter(offer -> offer.getStatus().equals(Status.CREATED))
                .toList();
        offerList.forEach(offer -> {
            Double possibleDistance = DistanceCalculator.calculateDistance(location, offer.getAddress().getLocation());
            if (possibleDistance < distance.get()) {
                distance.set(possibleDistance);
                closestOfferId.set(offer.getOfferId().getId());
            }
        });
        return closestOfferId.get();
    }
}
