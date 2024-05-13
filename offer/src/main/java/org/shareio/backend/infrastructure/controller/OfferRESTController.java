package org.shareio.backend.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.shareio.backend.Const;
import org.shareio.backend.controller.responses.CorrectResponse;
import org.shareio.backend.controller.responses.ErrorResponse;
import org.shareio.backend.core.model.vo.Category;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.usecases.port.dto.*;
import org.shareio.backend.core.usecases.port.in.*;
import org.shareio.backend.core.usecases.port.out.GetLocationDaoInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.infrastructure.dbadapter.repositories.OfferRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/offer/*")
public class OfferRESTController {
    AddOfferUseCaseInterface addOfferUseCaseInterface;
    GetUserProfileUseCaseInterface getUserProfileUseCaseInterface;
    GetLocationDaoInterface getLocationDaoInterface;
    GetOfferUseCaseInterface getOfferUseCaseInterface;
    GetClosestOfferUseCaseInterface getClosestOfferUseCaseInterface;
    ReserveOfferUseCaseInterface reserveOfferUseCaseInterface;
    OfferRepository offerRepository;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getOffer(@PathVariable(value = "id") UUID id) {
        try {
            OfferResponseDto offerResponseDto = getOfferUseCaseInterface.getOfferResponseDto(id);
            return new CorrectResponse(offerResponseDto, Const.successErrorCode, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        } catch (NoSuchElementException noSuchElementException) {
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/getConditions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getConditions() {
        List<ConditionWithDisplayName> conditionsWithDisplayNames = new ArrayList<>();
        for (Condition condition : Condition.values()) {
            conditionsWithDisplayNames.add(new ConditionWithDisplayName(condition));
        }
        return new CorrectResponse(new ConditionsResponseDto(conditionsWithDisplayNames), Const.successErrorCode, HttpStatus.OK);
    }

    @RequestMapping(value = "/getCategories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCategories() {
        List<CategoryWithDisplayName> categories = new ArrayList<>();
        for (Category category : Category.values()) {
            categories.add(new CategoryWithDisplayName(category));
        }
        return new CorrectResponse(new CategoriesResponseDto(categories), Const.successErrorCode, HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> searchOffersForList() {
        return new ErrorResponse(Const.notImplementedErrorCode, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/searchForMap", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> searchOffersForMap() {
        return new ErrorResponse(Const.notImplementedErrorCode, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addOffer(@RequestBody OfferSaveDto offerSaveDto) {
        try {
            getUserProfileUseCaseInterface.getUserProfileResponseDto(offerSaveDto.ownerId());
            OfferSaveResponseDto offerSaveResponseDto = addOfferUseCaseInterface.addOffer(offerSaveDto);
            return new CorrectResponse(offerSaveResponseDto, Const.successErrorCode, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException noSuchElementException) {
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/modify/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modifyOffer(@PathVariable(value = "id") UUID id) {
        return new ErrorResponse(Const.notImplementedErrorCode, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/reserve/{id}/{userId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> reserveOffer(@PathVariable(value = "id") UUID id, @PathVariable(value = "userId") UUID userId) {
        UUID offerId = reserveOfferUseCaseInterface.reserveOffer(id, userId);
        return new CorrectResponse(offerId, Const.successErrorCode, HttpStatus.OK);
    }

    @RequestMapping(value = "/getClosestOfferForUser/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getClosestOfferForUser(@PathVariable(value = "userId") UUID userId) {
        UserProfileResponseDto userProfileResponseDto;
        try {
            userProfileResponseDto = getUserProfileUseCaseInterface.getUserProfileResponseDto(userId);
        } catch (MultipleValidationException e) {
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        } catch (NoSuchElementException noSuchElementException) {
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
        }

        //TODO Move location validation to UserValidator
        Optional<LocationGetDto> locationGetDto = getLocationDaoInterface.getLocationDto(userProfileResponseDto.address().getId());
        if (locationGetDto.map(Location::fromDto).isPresent()) {
            UUID closestOfferId = getClosestOfferUseCaseInterface.getOfferResponseDto(locationGetDto.map(Location::fromDto).get());
            return new CorrectResponse(closestOfferId, Const.successErrorCode, HttpStatus.OK);
        } else {
            return new ErrorResponse(Const.toDoErrorCode, HttpStatus.FAILED_DEPENDENCY);
        }

    }
}