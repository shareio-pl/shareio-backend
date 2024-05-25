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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.NoSuchElementException;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/address/*")
public class AddressRESTController {
    GetAddressUseCaseInterface getAddressUseCaseInterface;
    GetLocationUseCaseInterface getLocationUseCaseInterface;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAddress(@PathVariable(value = "id") UUID id) {
        try {
            AddressResponseDto addressResponseDto = getAddressUseCaseInterface.getAddressResponseDto(id);
            return new CorrectResponse(addressResponseDto, Const.successErrorCode, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException noSuchElementException) {
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/location/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getLocationByAddressId(@PathVariable(value = "id") UUID id) {
        try {
            LocationResponseDto locationResponseDto = getLocationUseCaseInterface.getLocationResponseDto(id);
            return new CorrectResponse(locationResponseDto, Const.successErrorCode, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException noSuchElementException) {
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
        }
    }

//    @RequestMapping(value = "/modify/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Object> modifyAddress(HttpServletRequest httpRequest, @PathVariable(value = "id") UUID id) {
//        // TODO: check role in header
//        // if admin, execute request
//        // if user, check if address in user or address in any of user's offer, then execute request
//        log.error("ROLE {}", httpRequest.getHeaders("role").nextElement());
//        if (Objects.equals(httpRequest.getHeaders("role").nextElement(),"USER"))
//        {
//            return new CorrectResponse("USER", Const.successErrorCode, HttpStatus.OK);
//        }
//        else if (Objects.equals(httpRequest.getHeaders("role").nextElement(),"ADMIN")) {
//            return new CorrectResponse("ADMIN", Const.successErrorCode, HttpStatus.OK);
//        }
//        else {
//            return new ErrorResponse("NO ROLE", HttpStatus.BAD_REQUEST);
//        }
//    }
}
