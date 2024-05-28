package org.shareio.backend.core.usecases.service;

import lombok.AllArgsConstructor;
import org.shareio.backend.core.model.Offer;
import org.shareio.backend.core.model.User;
import org.shareio.backend.core.model.vo.Category;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.in.SearchOffersUseCaseInterface;
import org.shareio.backend.core.usecases.port.out.GetAllOffersDaoInterface;
import org.shareio.backend.core.usecases.port.out.GetUserProfileDaoInterface;
import org.shareio.backend.core.usecases.util.DistanceCalculator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Service
public class SearchOffersUseCaseService implements SearchOffersUseCaseInterface {

    GetUserProfileDaoInterface getUserProfileDaoInterface;
    GetAllOffersDaoInterface getAllOffersDaoInterface;

    @Override
    public List<UUID> getOfferListMeetingCriteria(UUID userId, String title, String category, String condition, Double distance, LocalDate endDate, List<UUID> userIdList) {
        User user = getUserProfileDaoInterface.getUserDto(userId).map(User::fromDto).orElseThrow(NoSuchElementException::new);
        List<OfferGetDto> allOfferDtoList= getAllOffersDaoInterface.getAllOffers();
        List<Offer> allOfferList = allOfferDtoList.stream().map(Offer::fromDto).toList();
        if (Objects.nonNull(title) && !title.isEmpty()){
            allOfferList = allOfferList.stream()
                    .filter(offer -> offer.getTitle().toUpperCase().startsWith(title.toUpperCase()) || offer.getTitle().toUpperCase().endsWith(title.toUpperCase()))
                    .toList();

        }
        if(Objects.nonNull(category) && !category.isEmpty()){
            allOfferList = allOfferList.stream()
                    .filter(offer -> Objects.equals(offer.getCategory(), Category.valueOf(category))).toList();
        }
        if(Objects.nonNull(condition) && !condition.isEmpty()){
            allOfferList = allOfferList.stream()
                    .filter(offer -> Objects.equals(offer.getCondition(), Condition.valueOf(condition))).toList();
        }
        if(Objects.nonNull(distance)){
            allOfferList = allOfferList.stream()
                    .filter(offer -> DistanceCalculator.calculateDistance(offer.getAddress().getLocation(), user.getAddress().getLocation()) < distance)
                    .toList();
        }
        if(Objects.nonNull(userIdList)){
            allOfferList = allOfferList.stream()
                    .filter(offer -> userIdList.contains(offer.getOwner().getUserId().getId()))
                    .toList();
        }
        if(Objects.nonNull(endDate)){
            allOfferList = allOfferList.stream()
                    .filter(offer -> offer.getCreationDate().isBefore(endDate.atStartOfDay())).toList();
        }

        //TODO: add sorting
        return allOfferList.stream().map(offer -> offer.getOfferId().getId()).toList();

    }
}
