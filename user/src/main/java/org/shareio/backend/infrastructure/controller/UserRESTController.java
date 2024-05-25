package org.shareio.backend.infrastructure.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shareio.backend.Const;
import org.shareio.backend.controller.responses.CorrectResponse;
import org.shareio.backend.controller.responses.ErrorResponse;
import org.shareio.backend.core.model.UserValidator;
import org.shareio.backend.core.usecases.port.dto.UserModifyDto;
import org.shareio.backend.core.usecases.port.dto.UserSaveDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileResponseDto;
import org.shareio.backend.core.usecases.port.in.AddUserUseCaseInterface;
import org.shareio.backend.core.usecases.port.in.GetUserProfileUseCaseInterface;
import org.shareio.backend.core.usecases.port.in.ModifyUserUseCaseInterface;
import org.shareio.backend.exceptions.LocationCalculationException;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.security.AuthenticationHandler;
import org.shareio.backend.security.RequestLogHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserRESTController {

    AuthenticationHandler authenticationHandler;

    AddUserUseCaseInterface addUserUseCaseInterface;
    GetUserProfileUseCaseInterface getUserProfileUseCaseInterface;
    ModifyUserUseCaseInterface modifyUserUseCaseInterface;
    
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addUser(HttpServletRequest httpRequest, @RequestBody UserSaveDto userSaveDto) {
        RequestLogHandler.handleRequest(httpRequest);
        try {
            UserValidator.validateUserSaveDto(userSaveDto);
            UUID createdUserUUID = addUserUseCaseInterface.addUser(userSaveDto);
            RequestLogHandler.handleCorrectResponse();
            return new CorrectResponse(createdUserUUID, Const.successErrorCode, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ErrorResponse(Const.illegalArgumentErrorCode, HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUser(HttpServletRequest httpRequest, @PathVariable(value = "id") UUID id) {
        RequestLogHandler.handleRequest(httpRequest);
        try {
            UserProfileResponseDto userProfileResponseDto = getUserProfileUseCaseInterface.getUserProfileResponseDto(id);
            RequestLogHandler.handleCorrectResponse();
            return new CorrectResponse(userProfileResponseDto, Const.successErrorCode, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/modify/{userId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modifyUser(HttpServletRequest httpRequest, @PathVariable(value = "userId") UUID userId, @RequestBody UserModifyDto userModifyDto) {
        RequestLogHandler.handleRequest(httpRequest);
        if(authenticationHandler.authenticateRequestForUserIdentity(httpRequest, userId)){
            try{
                modifyUserUseCaseInterface.modifyUser(userId, userModifyDto);
                UserProfileResponseDto userProfileResponseDto = getUserProfileUseCaseInterface.getUserProfileResponseDto(userId);
                RequestLogHandler.handleCorrectResponse();
                return new CorrectResponse(userProfileResponseDto, Const.successErrorCode, HttpStatus.OK);
            } catch (LocationCalculationException | IOException | InterruptedException e) {
                RequestLogHandler.handleErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
                return new ErrorResponse(Const.APINotRespondingErrorCode, HttpStatus.BAD_REQUEST);
            } catch (NoSuchElementException e) {
                RequestLogHandler.handleErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
                return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
            } catch (MultipleValidationException e) {
                RequestLogHandler.handleErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
                return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        else {
            return new ErrorResponse("Modification not allowed", HttpStatus.FORBIDDEN);
        }

    }

}

