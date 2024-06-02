package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.User;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.model.vo.OfferSortType;
import org.shareio.backend.core.usecases.port.dto.LocationResponseDto;
import org.shareio.backend.core.usecases.port.in.GetAllUserIdListUseCaseInterface;
import org.shareio.backend.core.usecases.port.in.GetAverageUserReviewValueUseCaseInterface;
import org.shareio.backend.core.usecases.port.in.GetLocationUseCaseInterface;
import org.shareio.backend.core.usecases.port.in.SearchOffersUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.core.usecases.util.DistanceCalculator;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@Service
public class SearchOffersUseCaseService implements SearchOffersUseCaseInterface {

    GetUserProfileDaoInterface getUserProfileDaoInterface;
    GetAllOffersDaoInterface getAllOffersDaoInterface;
    GetLocationUseCaseInterface getLocationUseCaseInterface;
    GetAllUserIdListUseCaseInterface getAllUserIdListUseCaseInterface;
    GetAverageUserReviewValueUseCaseInterface getAverageUserReviewValueUseCaseInterface;

    @Override
    public List<UUID> getOfferListMeetingCriteria(UUID userId, String title, List<String> category, String condition, Double distance, Double score, LocalDate creationDate, String sortType) {
        User user = getUserProfileDaoInterface.getUserDto(userId).map(User::fromDto).orElseThrow(NoSuchElementException::new);
        Location location;
        List<Offer> offers = getAllOffersDaoInterface.getAllOffers().stream().map(Offer::fromDto).toList();
        try {
            LocationResponseDto locationResponseDto = getLocationUseCaseInterface.getLocationResponseDto(user.getAddress().getAddressId().getId());
            location = new Location(locationResponseDto.latitude(), locationResponseDto.longitude());
        } catch (MultipleValidationException e) {
            return offers.stream().map(offer -> offer.getOfferId().getId()).toList();
        }
        offers = filterOfferList(offers, title, category, condition, distance, score, creationDate, location);
        offers = sortOfferList(offers, sortType, location);


        return offers.stream().map(offer -> offer.getOfferId().getId()).toList();
    }

    private List<Offer> sortOfferList(List<Offer> offers, String sortType, Location location) {
        if (Objects.nonNull(sortType)) {
            switch (OfferSortType.valueOf(sortType)) {
                case CLOSEST -> offers = offers.stream()
                        .sorted(Comparator.comparingDouble(
                                o -> DistanceCalculator.calculateDistance(
                                        o.getAddress().getLocation(), location)))
                        .toList().reversed();
                case FURTHEST -> offers = offers.stream()
                        .sorted(Comparator.comparingDouble(
                                o -> DistanceCalculator.calculateDistance(
                                        o.getAddress().getLocation(), location)))
                        .toList();
                case NEWEST -> offers = offers.stream()
                        .sorted(Comparator.comparing(Offer::getCreationDate))
                        .toList().reversed();
                case OLDEST -> offers = offers.stream()
                        .sorted(Comparator.comparing(Offer::getCreationDate))
                        .toList();
                case HIGHEST_RATED -> offers = offers.stream()
                        .sorted((o1, o2) ->
                                getAverageUserReviewValueUseCaseInterface.getAverageUserReviewValue(o1.getOwner().getUserId().getId())
                                        .compareTo(
                                                getAverageUserReviewValueUseCaseInterface.getAverageUserReviewValue(o2.getOwner().getUserId().getId())))
                        .toList();
            }
        }
        return offers;
    }

    private List<Offer> filterOfferList(List<Offer> offers, String title, List<String> category, String condition, Double distance, Double score, LocalDate creationDate, Location location) {

        // FILTER
        if (Objects.nonNull(title) && !title.isBlank()) {
            offers = offers.stream()
                    .filter(offer -> offer.getTitle().toUpperCase().contains(title.toUpperCase())).toList();
        }
        if (Objects.nonNull(category) && !category.isEmpty()) {
            offers = offers.stream()
                    .filter(offer -> category.contains(offer.getCategory().toString())).toList();
        }
        if (Objects.nonNull(condition) && !condition.isBlank()) {
            offers = offers.stream()
                    .filter(offer -> Objects.equals(offer.getCondition(), Condition.valueOf(condition))).toList();
        }
        if (Objects.nonNull(distance)) {
            offers = offers.stream()
                    .filter(offer -> DistanceCalculator.calculateDistance(offer.getAddress().getLocation(), location) <= distance)
                    .toList();
        }
        if (Objects.nonNull(score)) {
            List<UUID> userIds = getAllUserIdListUseCaseInterface.getAllUserIdList();
            Map<UUID, Double> userIdAndScoreMap = new HashMap<>();
            userIds.forEach(uuid -> userIdAndScoreMap.put(uuid, getAverageUserReviewValueUseCaseInterface.getAverageUserReviewValue(uuid)));
            List<UUID> filteredUserIds = userIdAndScoreMap.entrySet()
                    .stream()
                    .filter(mapUser -> mapUser.getValue() >= score)
                    .map(Map.Entry::getKey).toList();
            offers = offers.stream()
                    .filter(offer -> filteredUserIds.contains(offer.getOwner().getUserId().getId())).toList();
        }
        if (Objects.nonNull(creationDate)) {
            offers = offers.stream()
                    .filter(offer -> offer.getCreationDate().isBefore(creationDate.atStartOfDay())).toList();
        }
        return offers;
    }
}
