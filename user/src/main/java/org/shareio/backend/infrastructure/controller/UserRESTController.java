package org.shareio.backend.infrastructure.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shareio.backend.Const;
import org.shareio.backend.EnvGetter;
import org.shareio.backend.controller.responses.CorrectResponse;
import org.shareio.backend.controller.responses.ErrorResponse;
import org.shareio.backend.core.model.UserValidator;
import org.shareio.backend.core.usecases.port.dto.UserModifyDto;
import org.shareio.backend.core.usecases.port.dto.UserPasswordDto;
import org.shareio.backend.core.usecases.port.dto.UserSaveDto;
import org.shareio.backend.core.usecases.port.dto.UserProfileResponseDto;
import org.shareio.backend.core.usecases.port.in.*;
import org.shareio.backend.exceptions.LocationCalculationException;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.security.AuthenticationHandler;
import org.shareio.backend.security.RequestLogHandler;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/user")
@Slf4j
public class UserRESTController {

    AuthenticationHandler authenticationHandler;

    AddUserUseCaseInterface addUserUseCaseInterface;
    ChangePasswordUserUseCaseInterface changePasswordUserUseCaseInterface;
    GetUserProfileUseCaseInterface getUserProfileUseCaseInterface;
    ModifyUserUseCaseInterface modifyUserUseCaseInterface;
    GetAllUserIdListUseCaseInterface getAllUserIdListUseCaseInterface;
    SetProfilePhotoUseCaseInterface setProfilePhotoUseCaseInterface;


    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUser(HttpServletRequest httpRequest, @PathVariable(value = "id") UUID id) {
        RequestLogHandler.handleRequest(httpRequest);
        try {
            UserProfileResponseDto userProfileResponseDto = getUserProfileUseCaseInterface.getUserProfileResponseDto(id);
            RequestLogHandler.handleCorrectResponse(httpRequest);
            return new CorrectResponse(userProfileResponseDto, Const.SUCC_ERR, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.BAD_REQUEST, e.getMessage());
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.NOT_FOUND, e.getMessage());
            return new ErrorResponse(Const.NO_ELEM_ERR, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllUserIdList(HttpServletRequest httpRequest) {
        RequestLogHandler.handleRequest(httpRequest);
        List<UUID> userIdList = getAllUserIdListUseCaseInterface.getAllUserIdList();
        RequestLogHandler.handleCorrectResponse(httpRequest);
        return new CorrectResponse(userIdList, Const.SUCC_ERR, HttpStatus.OK);

    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addUser(HttpServletRequest httpRequest, @RequestBody UserSaveDto userSaveDto) {
        RequestLogHandler.handleRequest(httpRequest);
        try {
            UserValidator.validateUserSaveDto(userSaveDto);
            UUID createdUserUUID = addUserUseCaseInterface.addUser(userSaveDto);
            RequestLogHandler.handleCorrectResponse(httpRequest);
            return new CorrectResponse(createdUserUUID, Const.SUCC_ERR, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.BAD_REQUEST, e.getMessage());
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.BAD_REQUEST, e.getMessage());
            return new ErrorResponse(Const.ILL_ARG_ERR + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (LocationCalculationException e)
        {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.BAD_REQUEST, "Location error");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/setPhoto/{userId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> setUserProfilePhoto(HttpServletRequest httpRequest, @PathVariable(value = "userId") UUID userId, @RequestPart(value = "file") MultipartFile file) {
        RequestLogHandler.handleRequest(httpRequest);
        if (authenticationHandler.authenticateRequestForUserIdentity(httpRequest, userId)) {
            UUID newPhotoId = UUID.randomUUID(); // TODO: I think everything that works with files and shareio-image should be extracted into a class in lib
            String imageServiceUrl = EnvGetter.getImage();
            Resource fileResource = file.getResource();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileResource);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            String serverUrl = imageServiceUrl + "/image/createPNG/"; // TODO: change endpoint to call

            // Create new image
            RestTemplate restTemplate = new RestTemplate();
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(serverUrl + newPhotoId, requestEntity, String.class);

                if (!response.getStatusCode().is2xxSuccessful()) {
                    RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Photo could not be added");
                    return new ErrorResponse(Const.API_NOT_RESP_ERR + ": Photo could not be added", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } catch (Exception e) {
                RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Photo could not be added");
                return new ErrorResponse(Const.API_NOT_RESP_ERR + ": Photo could not be added", HttpStatus.INTERNAL_SERVER_ERROR);

            }

            // Set photoId in user
            UUID oldPhotoId = setProfilePhotoUseCaseInterface.setProfilePhoto(userId, newPhotoId);

            // Delete old photo
            if(oldPhotoId != Const.DEFAULT_PHOTO_ID){
                restTemplate = new RestTemplate();
                serverUrl = imageServiceUrl + "/image/delete/";
                try {

                    restTemplate.delete(serverUrl + oldPhotoId, String.class);

                } catch (Exception e) {
                    RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Photo could not be deleted");
                    return new ErrorResponse(Const.API_NOT_RESP_ERR + ": Photo could not be deleted", HttpStatus.INTERNAL_SERVER_ERROR);

                }
            }
            return new CorrectResponse(userId, Const.SUCC_ERR, HttpStatus.OK);
        } else {
            return new ErrorResponse("Changing profile photo not allowed", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping(value = "/modify/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modifyUser(HttpServletRequest httpRequest, @PathVariable(value = "userId") UUID userId, @RequestBody UserModifyDto userModifyDto) {
        RequestLogHandler.handleRequest(httpRequest);
        if (authenticationHandler.authenticateRequestForUserIdentity(httpRequest, userId)) {
            try {
                modifyUserUseCaseInterface.modifyUser(userId, userModifyDto);
                UserProfileResponseDto userProfileResponseDto = getUserProfileUseCaseInterface.getUserProfileResponseDto(userId);
                RequestLogHandler.handleCorrectResponse(httpRequest);
                return new CorrectResponse(userProfileResponseDto, Const.SUCC_ERR, HttpStatus.OK);
            } catch (NoSuchElementException e) {
                RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.NOT_FOUND, e.getMessage());
                return new ErrorResponse(Const.NO_ELEM_ERR, HttpStatus.NOT_FOUND);
            } catch (MultipleValidationException e) {
                RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.BAD_REQUEST, e.getMessage());
                return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ErrorResponse("Modification not allowed", HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping(value = "/changePassword/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> changePassword(HttpServletRequest httpRequest, @PathVariable(value = "userId") UUID userId, @RequestBody UserPasswordDto userPasswordDto) {
        RequestLogHandler.handleRequest(httpRequest);
        if (authenticationHandler.authenticateRequestForUserIdentity(httpRequest, userId)) {
            try {
                changePasswordUserUseCaseInterface.changePassword(userId, userPasswordDto);
                RequestLogHandler.handleCorrectResponse(httpRequest);
                return new CorrectResponse(userId, Const.SUCC_ERR, HttpStatus.OK);
            } catch (NoSuchElementException e) {
                RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.NOT_FOUND, e.getMessage());
                return new ErrorResponse(Const.NO_ELEM_ERR, HttpStatus.NOT_FOUND);
            } catch (IllegalArgumentException e) {
                RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.BAD_REQUEST, e.getMessage());
                return new ErrorResponse(Const.ILL_ARG_ERR, HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ErrorResponse("Modification not allowed", HttpStatus.FORBIDDEN);
        }
    }
}

