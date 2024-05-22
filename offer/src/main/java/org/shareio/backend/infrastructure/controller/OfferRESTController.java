package org.shareio.backend.infrastructure.controller;

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
import org.shareio.backend.exceptions.LocationCalculationException;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.infrastructure.dbadapter.repositories.OfferRepository;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.MediaType;
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
    ReserveOfferUseCaseInterface reserveOfferUseCaseInterface;
    GetOffersByNameUseCaseInterface getOffersByNameUseCaseInterface;
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

    @RequestMapping(value = "/getOffersByName", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getOffersByName(@RequestParam String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("offerIds", getOffersByNameUseCaseInterface.getOfferResponseDtoListByName(name));
            return new CorrectResponse(response, Const.successErrorCode, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        } catch (NoSuchElementException noSuchElementException) {
            return new ErrorResponse(Const.noSuchElementErrorCode, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> addOffer(@Valid @RequestPart("json") OfferSaveDto offerSaveDto, @RequestPart(value = "file", required = false) MultipartFile file) {
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

        String serverUrl = imageServiceUrl+ "/image/createPNG/" + photoId;

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate
                    .postForEntity(serverUrl, requestEntity, String.class);
            if (!response.getStatusCode().is2xxSuccessful()) {
                photoId = Const.defaultPhotoId;
            }
        } catch( Exception ex){
            log.error(Const.unsupportedMediaTypeErrorCode + ": Photo could not be added");
            photoId = Const.defaultPhotoId;
        }



        try {
            getUserProfileUseCaseInterface.getUserProfileResponseDto(offerSaveDto.ownerId());
            OfferSaveResponseDto offerSaveResponseDto = addOfferUseCaseInterface.addOffer(offerSaveDto, photoId);
            return new CorrectResponse(offerSaveResponseDto, Const.successErrorCode, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (LocationCalculationException e) {
            return new ErrorResponse(Const.cannotDetermineAddressErrorCode, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error(e.toString());
            return new ErrorResponse(Const.APINotRespondingErrorCode, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/modify/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modifyOffer(@PathVariable(value = "id") UUID id) {
        return new ErrorResponse(Const.notImplementedErrorCode, HttpStatus.NOT_IMPLEMENTED);
    }

    @RequestMapping(value = "/reserve", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> reserveOffer(@RequestBody OfferReserveDto offerReserveDto) {
        UUID offerId = reserveOfferUseCaseInterface.reserveOffer(offerReserveDto);
        return new CorrectResponse(offerId, Const.successErrorCode, HttpStatus.OK);
    }

    @RequestMapping(value = "/addReview", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addReviewToOffer(@RequestBody OfferReviewDto offerReviewDto) {
        UUID reviewId = addReviewUseCaseInterface.addReview(offerReviewDto);
        return new CorrectResponse(reviewId, Const.successErrorCode, HttpStatus.OK);
    }
}