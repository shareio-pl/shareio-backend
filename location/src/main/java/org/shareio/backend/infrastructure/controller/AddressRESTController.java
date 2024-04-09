package org.shareio.backend.infrastructure.controller;

import org.shareio.backend.Const;
import org.shareio.backend.controller.responses.CorrectResponse;
import org.shareio.backend.controller.responses.ErrorResponse;
import org.shareio.backend.core.usecases.port.dto.AddressResponseDto;
import org.shareio.backend.core.usecases.port.dto.LocationResponseDto;
import org.shareio.backend.core.usecases.port.in.GetAddressUseCaseInterface;
import org.shareio.backend.core.usecases.port.in.GetLocationUseCaseInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.NoSuchElementException;
import java.util.UUID;

public class AddressRESTController {
    GetAddressUseCaseInterface getAddressUseCaseInterface;
    GetLocationUseCaseInterface getLocationUseCaseInterface;

    @RequestMapping(value = "/address", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getAddressById(@RequestParam UUID id) {
        try {
            AddressResponseDto addressResponseDto = getAddressUseCaseInterface.getAddressResponseDto(id);
            return new CorrectResponse(addressResponseDto, "Correct address", HttpStatus.OK);
        } catch (MultipleValidationException e) {
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException noSuchElementException) {
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/location", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getLocationByAddressId(@RequestParam UUID id) {
        try {
            LocationResponseDto locationResponseDto = getLocationUseCaseInterface.getLocationResponseDto(id);
            return new CorrectResponse(locationResponseDto, "Correct location", HttpStatus.OK);
        } catch (MultipleValidationException e) {
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException noSuchElementException) {
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.BAD_REQUEST);
        }
    }
}
