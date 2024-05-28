package org.shareio.backend.infrastructure.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.shareio.backend.Const;
import org.shareio.backend.controller.responses.CorrectResponse;
import org.shareio.backend.controller.responses.ErrorResponse;
import org.shareio.backend.core.usecases.port.dto.RemoveResponseDto;
import org.shareio.backend.core.usecases.port.in.*;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.security.RequestLogHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@RestController
public class DeleteRESTController {

    GetUserProfileUseCaseInterface getUserProfileUseCase;

    RemoveUserUseCaseInterface removeUserUseCaseInterface;
    RemoveOffersForUserUseCaseInterface removeOffersForUserUseCaseInterface;

    GetOfferUseCaseInterface getOfferUseCaseInterface;

    RemoveOfferUseCaseInterface removeOfferUseCaseInterface;

    @DeleteMapping(value = "/user/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteUser(HttpServletRequest httpRequest, @PathVariable(value = "id") UUID id) {
        try{
            RequestLogHandler.handleRequest(httpRequest);
            getUserProfileUseCase.getUserProfileResponseDto(id);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(httpRequest,HttpStatus.FAILED_DEPENDENCY, "User with id: "+id+" is malformed");
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        } catch (NoSuchElementException e) {
            RequestLogHandler.handleErrorResponse(httpRequest,HttpStatus.NOT_FOUND, "User with id: "+id+" not found");
            return new ErrorResponse(Const.NO_ELEM_ERR, HttpStatus.NOT_FOUND);
        }
        RemoveResponseDto removeResponseDto = new RemoveResponseDto();
        removeResponseDto = removeOffersForUserUseCaseInterface.removeOffersForUser(id, removeResponseDto);
        removeResponseDto = removeUserUseCaseInterface.removeUser(id, removeResponseDto);
        RequestLogHandler.handleCorrectResponse(httpRequest);
        return new CorrectResponse(removeResponseDto, Const.SUCC_ERR, HttpStatus.OK);
    }

    @DeleteMapping(value = "/offer/delete/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteOffer(HttpServletRequest httpRequest, @PathVariable(value = "id") UUID id) {
        try{
            RequestLogHandler.handleRequest(httpRequest);
            getOfferUseCaseInterface.getOfferResponseDto(id, null, null);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(httpRequest,HttpStatus.FAILED_DEPENDENCY, "Offer with id: "+id+" is malformed");
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        } catch (NoSuchElementException e) {
            RequestLogHandler.handleErrorResponse(httpRequest,HttpStatus.NOT_FOUND, "Offer with id: "+id+" not found");
            return new ErrorResponse(Const.NO_ELEM_ERR, HttpStatus.NOT_FOUND);
        }
        RemoveResponseDto removeResponseDto = new RemoveResponseDto();
        removeResponseDto = removeOfferUseCaseInterface.removeOffer(id, removeResponseDto);
        RequestLogHandler.handleCorrectResponse(httpRequest);
        return new CorrectResponse(removeResponseDto, Const.SUCC_ERR, HttpStatus.OK);
    }
}
