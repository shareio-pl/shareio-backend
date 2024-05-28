package org.shareio.backend.infrastructure.controller;

import lombok.AllArgsConstructor;
import org.shareio.backend.Const;
import org.shareio.backend.controller.responses.CorrectResponse;
import org.shareio.backend.controller.responses.ErrorResponse;
import org.shareio.backend.core.usecases.port.dto.AddressResponseDto;
import org.shareio.backend.core.usecases.port.dto.LocationResponseDto;
import org.shareio.backend.core.usecases.port.in.GetAddressUseCaseInterface;
import org.shareio.backend.core.usecases.port.in.GetLocationUseCaseInterface;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/address/*")
public class AddressRESTController {
    GetAddressUseCaseInterface getAddressUseCaseInterface;
    GetLocationUseCaseInterface getLocationUseCaseInterface;

    @GetMapping(value = "/get/{id}",  produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAddress(@PathVariable(value = "id") UUID id) {
        try {
            AddressResponseDto addressResponseDto = getAddressUseCaseInterface.getAddressResponseDto(id);
            return new CorrectResponse(addressResponseDto, Const.SUCC_ERR, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException noSuchElementException) {
            return new ErrorResponse(Const.NO_ELEM_ERR +": NO ADDRESS FOUND", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/location/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getLocationByAddressId(@PathVariable(value = "id") UUID id) {
        try {
            LocationResponseDto locationResponseDto = getLocationUseCaseInterface.getLocationResponseDto(id);
            return new CorrectResponse(locationResponseDto, Const.SUCC_ERR, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException noSuchElementException) {
            return new ErrorResponse(Const.NO_ELEM_ERR +": NO LOCATION FOUND", HttpStatus.NOT_FOUND);
        }
    }

}
