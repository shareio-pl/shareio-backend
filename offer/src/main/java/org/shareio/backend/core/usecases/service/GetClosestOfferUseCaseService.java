package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
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
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class GetClosestOfferUseCaseService implements GetClosestOfferUseCaseInterface {
    GetAllOffersDaoInterface getAllOffersDaoInterface;
    GetLocationDaoInterface getLocationDaoInterface;

    @Override
    public UUID getOfferResponseDto(Location location) throws NoSuchElementException {
        List<OfferGetDto> allOfferList = getAllOffersDaoInterface.getAllOffers();
        AtomicReference<UUID> closestOfferId = new AtomicReference<>();
        AtomicReference<Double> distance = new AtomicReference<>(Double.MAX_VALUE);
        allOfferList = allOfferList.stream().filter(offer -> {
            try {
                OfferValidator.validateOffer(offer);
                return true;
            } catch (MultipleValidationException e) {
                return false;
            }
        }).toList();
        if (allOfferList.isEmpty()) {
            throw new NoSuchElementException("No valid offers found!");
        }
        List<Offer> offerList = allOfferList.stream().map(Offer::fromDto).toList();
        //TODO: Degree distance to KM
        offerList.forEach(offer -> {
            Double possibleDistance = calculateDistance(location, offer.getAddress().getLocation());
            if (possibleDistance < distance.get()) {
                distance.set(possibleDistance);
                closestOfferId.set(offer.getOfferId().getId());
            }
        });
        return closestOfferId.get();
    }

    private Double calculateDistance(Location l1, Location l2) {
        return Math.sqrt(
                Math.pow((l1.getLatitude() - l2.getLatitude()), 2)
                        +
                        Math.pow((l1.getLongitude() - l2.getLongitude()), 2)
        );
    }
}
