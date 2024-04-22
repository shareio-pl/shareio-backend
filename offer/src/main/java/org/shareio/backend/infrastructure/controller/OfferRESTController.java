package org.shareio.backend.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.shareio.backend.Const;
import org.shareio.backend.controller.responses.CorrectResponse;
import org.shareio.backend.controller.responses.ErrorResponse;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.usecases.port.dto.ConditionsResponseDto;
import org.shareio.backend.core.usecases.port.dto.OfferResponseDto;
import org.shareio.backend.core.usecases.port.in.GetOfferUseCaseInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.infrastructure.dbadapter.repositories.OfferRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/offer/*")
public class OfferRESTController {
    GetOfferUseCaseInterface getOfferUseCaseInterface;
    OfferRepository offerRepository;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getOffer(@PathVariable(value = "id") UUID id) {
        try {
            OfferResponseDto offerResponseDto = getOfferUseCaseInterface.getOfferResponseDto(id);
            return new CorrectResponse(offerResponseDto, Const.successErrorCode, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException noSuchElementException) {
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/getConditions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getConditions() {
        List<Condition> conditions = Arrays.asList(Condition.values());
        return new CorrectResponse(new ConditionsResponseDto(conditions), Const.successErrorCode, HttpStatus.OK);
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
    public ResponseEntity<Object> addOffer() {
        return new ErrorResponse(Const.notImplementedErrorCode, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/modify/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modifyOffer(@PathVariable(value = "id") UUID id) {
        return new ErrorResponse(Const.notImplementedErrorCode, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/reserve/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> reserveOffer(@PathVariable(value = "id") UUID id) {
        return new ErrorResponse(Const.notImplementedErrorCode, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> deleteOffer(@PathVariable(value = "id") UUID id) {
        return new ErrorResponse(Const.notImplementedErrorCode, HttpStatus.NOT_IMPLEMENTED);
    }
}