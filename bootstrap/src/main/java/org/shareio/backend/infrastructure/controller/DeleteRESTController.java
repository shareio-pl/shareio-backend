package org.shareio.backend.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.shareio.backend.Const;
import org.shareio.backend.controller.responses.CorrectResponse;
import org.shareio.backend.controller.responses.ErrorResponse;
import org.shareio.backend.core.usecases.port.dto.RemoveResponseDto;
import org.shareio.backend.core.usecases.port.in.*;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@RestController
public class DeleteRESTController {

    GetUserProfileUseCaseInterface getUserProfileUseCase;

    RemoveUserUseCaseInterface removeUserUseCaseInterface;
    RemoveOffersForUserUseCaseInterface removeOffersForUserUseCaseInterface;
//    RemoveReviewsForUserUseCaseInterface removeReviewsForUserUseCaseInterface;

    GetOfferUseCaseInterface getOfferUseCaseInterface;

    RemoveOfferUseCaseInterface removeOfferUseCaseInterface;
//    RemoveReviewsForOfferUseCaseInterface removeReviewsForOfferUseCaseInterface;

    @RequestMapping(value = "/user/delete/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") UUID id) {
        try{
            getUserProfileUseCase.getUserProfileResponseDto(id);
        } catch (MultipleValidationException e) {
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        } catch (NoSuchElementException e) {
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
        }
        RemoveResponseDto removeResponseDto = new RemoveResponseDto();
//        removeResponseDto = removeReviewsForUserUseCaseInterface.removeReviewsForUser(id, removeResponseDto);
        removeResponseDto = removeOffersForUserUseCaseInterface.removeOffersForUser(id, removeResponseDto);
        removeResponseDto = removeUserUseCaseInterface.removeUser(id, removeResponseDto);
        return new CorrectResponse(removeResponseDto, Const.successErrorCode, HttpStatus.OK);
    }

    @RequestMapping(value = "/offer/delete/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteOffer(@PathVariable(value = "id") UUID id) {
        try{
            getOfferUseCaseInterface.getOfferResponseDto(id, null, null);
        } catch (MultipleValidationException e) {
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        } catch (NoSuchElementException e) {
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
        }
        RemoveResponseDto removeResponseDto = new RemoveResponseDto();
//        removeResponseDto = removeReviewsForOfferUseCaseInterface.removeReviewsForOffer(id, removeResponseDto);
        removeResponseDto = removeOfferUseCaseInterface.removeOffer(id, removeResponseDto);
        return new CorrectResponse(removeResponseDto, Const.successErrorCode, HttpStatus.OK);
    }
}
