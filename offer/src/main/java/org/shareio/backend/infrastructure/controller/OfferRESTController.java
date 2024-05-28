package org.shareio.backend.infrastructure.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shareio.backend.Const;
import org.shareio.backend.EnvGetter;
import org.shareio.backend.controller.responses.CorrectResponse;
import org.shareio.backend.controller.responses.ErrorResponse;
import org.shareio.backend.core.model.vo.Category;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.usecases.port.dto.*;
import org.shareio.backend.core.usecases.port.in.*;
import org.shareio.backend.core.usecases.port.out.GetLocationDaoInterface;
import org.shareio.backend.core.usecases.service.GetNewestOffersUseCaseService;
import org.shareio.backend.exceptions.LocationCalculationException;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.external_API.GPT.DescriptionGenerator;
import org.shareio.backend.infrastructure.dbadapter.repositories.OfferRepository;
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
@RequestMapping(value = "/offer/*")
@Slf4j
public class OfferRESTController {
    AddOfferUseCaseInterface addOfferUseCaseInterface;
    AddReviewUseCaseInterface addReviewUseCaseInterface;

    GetUserProfileUseCaseInterface getUserProfileUseCaseInterface;
    GetLocationDaoInterface getLocationDaoInterface;
    GetOfferUseCaseInterface getOfferUseCaseInterface;
    GetClosestOfferUseCaseInterface getClosestOfferUseCaseInterface;
    GetOffersByUserUseCaseInterface getOffersByUserUseCaseInterface;
    GetOffersByNameUseCaseInterface getOffersByNameUseCaseInterface;
    GetOwnerReviewCountUseCaseInterface getOwnerReviewCountUseCaseInterface;
    GetAverageUserReviewValueUseCaseInterface getAverageUserReviewValueUseCaseInterface;
    GetNewestOffersUseCaseService getNewestOffersUseCaseService;

    ModifyOfferUseCaseInterface modifyOfferUseCaseInterface;

    ReserveOfferUseCaseInterface reserveOfferUseCaseInterface;

    OfferRepository offerRepository;
    AuthenticationHandler authenticationHandler;

    // ------------------- GET -------------------

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getOffer(HttpServletRequest httpRequest, @PathVariable(value = "id") UUID id) {
        RequestLogHandler.handleRequest(httpRequest);
        try {
            Integer reviewCount = getOwnerReviewCountUseCaseInterface.getUserReviewCount(id);
            Double averageUserReviewValue = getAverageUserReviewValueUseCaseInterface.getAverageUserReviewValue(id);
            OfferResponseDto offerResponseDto = getOfferUseCaseInterface.getOfferResponseDto(id, reviewCount, averageUserReviewValue);
            RequestLogHandler.handleCorrectResponse();
            return new CorrectResponse(offerResponseDto, Const.successErrorCode, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.FAILED_DEPENDENCY, e.getMessage());
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        } catch (NoSuchElementException noSuchElementException) {
            RequestLogHandler.handleErrorResponse(HttpStatus.NOT_FOUND, "Entity not found");
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(value = "/getClosestOfferForUser/{userId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getClosestOfferForUser(HttpServletRequest httpRequest, @PathVariable(value = "userId") UUID userId) {
        RequestLogHandler.handleRequest(httpRequest);
        UserProfileResponseDto userProfileResponseDto;
        try {
            userProfileResponseDto = getUserProfileUseCaseInterface.getUserProfileResponseDto(userId);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.FAILED_DEPENDENCY, e.getMessage());
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        } catch (NoSuchElementException noSuchElementException) {
            RequestLogHandler.handleErrorResponse(HttpStatus.NOT_FOUND, "Entity not found");
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
        }

        //TODO Move location validation to UserValidator
        Optional<LocationGetDto> locationGetDto = getLocationDaoInterface.getLocationDto(userProfileResponseDto.address().getId());
        if (locationGetDto.map(Location::fromDto).isPresent()) {
            UUID closestOfferId = getClosestOfferUseCaseInterface.getOfferResponseDto(locationGetDto.map(Location::fromDto).get());
            RequestLogHandler.handleCorrectResponse();
            return new CorrectResponse(closestOfferId, Const.successErrorCode, HttpStatus.OK);
        } else {
            RequestLogHandler.handleErrorResponse(HttpStatus.FAILED_DEPENDENCY, "Database data error");
            return new ErrorResponse(Const.toDoErrorCode, HttpStatus.FAILED_DEPENDENCY);
        }

    }

    @RequestMapping(value = "/getOffersByUser/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getOffersByUser(HttpServletRequest httpRequest, @PathVariable(value = "id") UUID id) {
        RequestLogHandler.handleRequest(httpRequest);
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("offerIds", getOffersByUserUseCaseInterface.getOfferResponseDtoListByUser(id));
            RequestLogHandler.handleCorrectResponse();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            response.put("error", e.getMessage());
            RequestLogHandler.handleErrorResponse(HttpStatus.FAILED_DEPENDENCY, "Database data error");
            return new ResponseEntity<>(response, HttpStatus.FAILED_DEPENDENCY);
        } catch (NoSuchElementException e) {
            response.put("error", e.getMessage());
            RequestLogHandler.handleErrorResponse(HttpStatus.NOT_FOUND, "Entity not found");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.put("error", e.getMessage());
            RequestLogHandler.handleErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/getOffersByName", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getOffersByName(HttpServletRequest httpRequest, @RequestParam String name) {
        RequestLogHandler.handleRequest(httpRequest);
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("offerIds", getOffersByNameUseCaseInterface.getOfferResponseDtoListByName(name));
            RequestLogHandler.handleCorrectResponse();
            return new CorrectResponse(response, Const.successErrorCode, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.FAILED_DEPENDENCY, "Database data error");
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        } catch (NoSuchElementException noSuchElementException) {
            RequestLogHandler.handleErrorResponse(HttpStatus.NOT_FOUND, "Entity not found");
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
            return new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/getCategories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCategories(HttpServletRequest httpRequest) {
        RequestLogHandler.handleRequest(httpRequest);
        List<CategoryWithDisplayName> categories = new ArrayList<>();
        for (Category category : Category.values()) {
            categories.add(new CategoryWithDisplayName(category));
        }
        RequestLogHandler.handleCorrectResponse();
        return new CorrectResponse(new CategoriesResponseDto(categories), Const.successErrorCode, HttpStatus.OK);
    }

    @RequestMapping(value = "/getConditions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getConditions(HttpServletRequest httpRequest) {
        RequestLogHandler.handleRequest(httpRequest);
        List<ConditionWithDisplayName> conditionsWithDisplayNames = new ArrayList<>();
        for (Condition condition : Condition.values()) {
            conditionsWithDisplayNames.add(new ConditionWithDisplayName(condition));
        }
        RequestLogHandler.handleCorrectResponse();
        return new CorrectResponse(new ConditionsResponseDto(conditionsWithDisplayNames), Const.successErrorCode, HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> searchOffersForList(HttpServletRequest httpRequest) {
        RequestLogHandler.handleRequest(httpRequest);
        //TODO
        return new ErrorResponse(Const.notImplementedErrorCode, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/searchForMap", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> searchOffersForMap(HttpServletRequest httpRequest) {
        RequestLogHandler.handleRequest(httpRequest);
        //TODO
        return new ErrorResponse(Const.notImplementedErrorCode, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/generateDescription", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> generateDescription(HttpServletRequest httpRequest, @RequestParam String title, @RequestParam String condition, @RequestParam String category, @RequestParam(required = false) String additionalData) {
        RequestLogHandler.handleRequest(httpRequest);
        DescriptionGenerator generator = new DescriptionGenerator();
        String description;
        try {
            if (additionalData == null) {
                description = generator.generateDescription(title, condition, category);
            } else {
                description = generator.generateDescription(title, condition, category, additionalData);
            }
            RequestLogHandler.handleCorrectResponse();
            return new CorrectResponse(description, Const.successErrorCode, HttpStatus.OK);
        } catch (Exception e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
            return new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/getNewest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getNewestOffers(HttpServletRequest httpRequest) {
        RequestLogHandler.handleRequest(httpRequest);
        try{
            List<UUID> newestOffersId = getNewestOffersUseCaseService.getNewestOffers();
            RequestLogHandler.handleCorrectResponse();
            return new CorrectResponse(newestOffersId, Const.successErrorCode, HttpStatus.OK);

        } catch(IllegalArgumentException illegalArgumentException){
            RequestLogHandler.handleErrorResponse(HttpStatus.BAD_REQUEST, illegalArgumentException.getMessage());
            return new ErrorResponse(Const.illegalArgumentErrorCode, HttpStatus.BAD_REQUEST);
        }
     }

    // ------------------- POST -------------------


    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> addOffer(HttpServletRequest httpRequest, @Valid @RequestPart("json") OfferSaveDto offerSaveDto, @RequestPart(value = "file") MultipartFile file) {
        RequestLogHandler.handleRequest(httpRequest);
        UUID photoId = UUID.randomUUID();
        String imageServiceUrl = EnvGetter.getImage();
        Resource fileResource = file.getResource();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", fileResource);

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);

        String serverUrl = imageServiceUrl + "/image/createPNG/" + photoId;

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate
                    .postForEntity(serverUrl, requestEntity, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                RequestLogHandler.handleErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Photo could not be added");
                return new ErrorResponse(Const.APINotRespondingErrorCode + ": Photo could not be added", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Photo could not be added");
            return new ErrorResponse(Const.APINotRespondingErrorCode + ": Photo could not be added", HttpStatus.INTERNAL_SERVER_ERROR);

        }

        try {
            getUserProfileUseCaseInterface.getUserProfileResponseDto(offerSaveDto.ownerId());
            OfferSaveResponseDto offerSaveResponseDto = addOfferUseCaseInterface.addOffer(offerSaveDto, photoId);
            RequestLogHandler.handleCorrectResponse();
            return new CorrectResponse(offerSaveResponseDto, Const.successErrorCode, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.BAD_REQUEST, "Validation error");
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (LocationCalculationException e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.BAD_REQUEST, "Location calculation error");
            return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
            return new ErrorResponse(Const.APINotRespondingErrorCode, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/reserve", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> reserveOffer(HttpServletRequest httpRequest, @RequestBody OfferReserveDto offerReserveDto) {
        RequestLogHandler.handleRequest(httpRequest);
        try {
            if (authenticationHandler.authenticateRequestForUserIdentity(httpRequest, offerReserveDto.recieverId())) {
                UUID offerId = reserveOfferUseCaseInterface.reserveOffer(offerReserveDto);
                RequestLogHandler.handleCorrectResponse();
                return new CorrectResponse(offerId, Const.successErrorCode, HttpStatus.OK);
            } else {
                RequestLogHandler.handleErrorResponse(HttpStatus.FORBIDDEN, "NO PERMISSIONS");
                return new ErrorResponse("NO PERMISSIONS", HttpStatus.FORBIDDEN);
            }
        } catch (NoSuchElementException noSuchElementException) {
            RequestLogHandler.handleErrorResponse(HttpStatus.NOT_FOUND, "Entity not found");
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.FAILED_DEPENDENCY, "Database data error");
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        }

    }


    @RequestMapping(value = "/addReview", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addReviewToOffer(HttpServletRequest httpRequest, @RequestBody OfferReviewDto offerReviewDto) {
        RequestLogHandler.handleRequest(httpRequest);
        try {
            UUID reviewId = addReviewUseCaseInterface.addReview(offerReviewDto);
            RequestLogHandler.handleCorrectResponse();
            return new CorrectResponse(reviewId, Const.successErrorCode, HttpStatus.OK);
        } catch (NoSuchElementException noSuchElementException) {
            RequestLogHandler.handleErrorResponse(HttpStatus.NOT_FOUND, "Entity not found");
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.FAILED_DEPENDENCY, "Database data error");
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);

        }
    }

    // ------------------- PUT -------------------


    @RequestMapping(value = "/modify/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modifyOffer(HttpServletRequest httpRequest, @PathVariable(value = "id") UUID offerId, @RequestBody OfferModifyDto offerModifyDto) {
        RequestLogHandler.handleRequest(httpRequest);
        try {
            modifyOfferUseCaseInterface.modifyOffer(offerId, offerModifyDto);
            RequestLogHandler.handleCorrectResponse();
            return new CorrectResponse(offerId, Const.successErrorCode, HttpStatus.OK);
        } catch (NoSuchElementException noSuchElementException) {
            RequestLogHandler.handleErrorResponse(HttpStatus.NOT_FOUND, "Entity not found");
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.BAD_REQUEST, "Validation error");
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (LocationCalculationException e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.BAD_REQUEST, "Location calculation error");
            return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            RequestLogHandler.handleErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");
            return new ErrorResponse(Const.APINotRespondingErrorCode, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}