package org.shareio.backend.infrastructure.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shareio.backend.Const;
import org.shareio.backend.EnvGetter;
import org.shareio.backend.controller.responses.CorrectResponse;
import org.shareio.backend.controller.responses.ErrorResponse;
import org.shareio.backend.controller.responses.ShareioResponse;
import org.shareio.backend.core.model.vo.Category;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.*;
import org.shareio.backend.core.usecases.port.in.*;
import org.shareio.backend.core.usecases.port.out.GetLocationDaoInterface;
import org.shareio.backend.core.usecases.service.GetNewestOffersUseCaseService;
import org.shareio.backend.exceptions.DescriptionGenerationException;
import org.shareio.backend.exceptions.LocationCalculationException;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.external.gpt.DescriptionGenerator;
import org.shareio.backend.infrastructure.dbadapter.repositories.OfferRepository;
import org.shareio.backend.security.AuthenticationHandler;
import org.shareio.backend.security.IdentityHandler;
import org.shareio.backend.security.RequestLogHandler;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/offer/*")
@Slf4j
public class OfferRESTController {
    AddOfferUseCaseInterface addOfferUseCaseInterface;
    AddReviewUseCaseInterface addReviewUseCaseInterface;

    GetAllOffersUseCaseInterface getAllOffersUseCaseInterface;
    GetUserProfileUseCaseInterface getUserProfileUseCaseInterface;
    GetLocationDaoInterface getLocationDaoInterface;
    GetOfferUseCaseInterface getOfferUseCaseInterface;
    GetClosestOfferUseCaseInterface getClosestOfferUseCaseInterface;
    GetOffersByUserAndStatusUseCaseInterface getOffersByUserAndStatusUseCaseInterface;
    GetOffersByNameUseCaseInterface getOffersByNameUseCaseInterface;
    GetOwnerReviewCountUseCaseInterface getOwnerReviewCountUseCaseInterface;
    GetAverageUserReviewValueUseCaseInterface getAverageUserReviewValueUseCaseInterface;
    GetNewestOffersUseCaseService getNewestOffersUseCaseService;
    GetAllUserIdListUseCaseInterface getAllUserIdListUseCaseInterface;
    SearchOffersUseCaseInterface searchOffersUseCaseInterface;
    GetOfferOwnerIdUseCaseInterface getOfferOwnerIdUseCaseInterface;
    GetOffersByReceiverAndStatusUseCaseInterface getOffersByReceiverAndStatusUseCaseInterface;

    ModifyOfferUseCaseInterface modifyOfferUseCaseInterface;

    CancelOfferUseCaseInterface cancelOfferUseCaseInterface;
    FinishOfferUseCaseInterface finishOfferUseCaseInterface;
    ReserveOfferUseCaseInterface reserveOfferUseCaseInterface;

    OfferRepository offerRepository;
    AuthenticationHandler authenticationHandler;
    IdentityHandler identityHandler;

    // ------------------- GET -------------------

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getOffer(HttpServletRequest httpRequest, @PathVariable(value = "id") UUID id) {
        RequestLogHandler.handleRequest(httpRequest);
        UUID userId = identityHandler.getUserIdFromHeader(httpRequest);
        try {
            if (Objects.nonNull(userId)) {
                log.error(userId.toString());
            }
            UUID ownerId = getOfferOwnerIdUseCaseInterface.getOfferOwnerId(id);
            Integer reviewCount = getOwnerReviewCountUseCaseInterface.getUserReviewCount(ownerId);
            Double averageUserReviewValue = getAverageUserReviewValueUseCaseInterface.getAverageUserReviewValue(ownerId);
            OfferResponseDto offerResponseDto = getOfferUseCaseInterface.getOfferResponseDto(id, userId, reviewCount, averageUserReviewValue);
            RequestLogHandler.handleCorrectResponse(httpRequest);
            return new CorrectResponse(offerResponseDto, Const.SUCC_ERR, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.FAILED_DEPENDENCY, e.getMessage());
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        } catch (NoSuchElementException noSuchElementException) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.NOT_FOUND, Const.NO_ELEM_ERR);
            return new ErrorResponse(Const.NO_ELEM_ERR, HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping(value = "/getClosestOfferForUser/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getClosestOfferForUser(HttpServletRequest httpRequest, @PathVariable(value = "userId") UUID userId) {
        RequestLogHandler.handleRequest(httpRequest);
        UserProfileResponseDto userProfileResponseDto;
        try {
            userProfileResponseDto = getUserProfileUseCaseInterface.getUserProfileResponseDto(userId);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.FAILED_DEPENDENCY, e.getMessage());
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        } catch (NoSuchElementException noSuchElementException) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.NOT_FOUND, Const.NO_ELEM_ERR);
            return new ErrorResponse(Const.NO_ELEM_ERR, HttpStatus.NOT_FOUND);
        }
        Optional<LocationGetDto> locationGetDto = getLocationDaoInterface.getLocationDto(userProfileResponseDto.address().getId());
        if (locationGetDto.map(Location::fromDto).isPresent()) {
            UUID closestOfferId = getClosestOfferUseCaseInterface.getOfferResponseDto(locationGetDto.map(Location::fromDto).orElseThrow(NoSuchElementException::new));
            RequestLogHandler.handleCorrectResponse(httpRequest);
            return new CorrectResponse(closestOfferId, Const.SUCC_ERR, HttpStatus.OK);
        } else {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.FAILED_DEPENDENCY, Const.DATA_INTEGRITY_ERR);
            return new ErrorResponse(Const.TO_DO_ERR, HttpStatus.FAILED_DEPENDENCY);
        }

    }

    @GetMapping(value = "/getCreatedOffersByUser/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCreatedOffersByUser(HttpServletRequest httpRequest, @PathVariable(value = "userId") UUID id) {
        return getOfferListBasedOnStatus(httpRequest, id, Status.CREATED);
    }

    @GetMapping(value = "/getReservedOffersByUser/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getReservedOffersByUser(HttpServletRequest httpRequest, @PathVariable(value = "userId") UUID id) {
        return getOfferListBasedOnStatus(httpRequest, id, Status.RESERVED);
    }

    @GetMapping(value = "/getFinishedOffersByUser/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getFinishedOffersByUser(HttpServletRequest httpRequest, @PathVariable(value = "userId") UUID id) {
        return getOfferListBasedOnStatus(httpRequest, id, Status.FINISHED);
    }

    @GetMapping(value = "/getOffersByName", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getOffersByName(HttpServletRequest httpRequest, @RequestParam String name) {
        RequestLogHandler.handleRequest(httpRequest);
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("offerIds", getOffersByNameUseCaseInterface.getOfferResponseDtoListByName(name));
            RequestLogHandler.handleCorrectResponse(httpRequest);
            return new CorrectResponse(response, Const.SUCC_ERR, HttpStatus.OK);
        } catch (NoSuchElementException noSuchElementException) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.NOT_FOUND, Const.NO_ELEM_ERR);
            return new ErrorResponse(Const.NO_ELEM_ERR, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.INTERNAL_SERVER_ERROR, Const.SERVER_ERR +":  "+ e.getMessage());
            return new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getCategories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getCategories(HttpServletRequest httpRequest) {
        RequestLogHandler.handleRequest(httpRequest);
        List<CategoryWithDisplayName> categories = new ArrayList<>();
        for (Category category : Category.values()) {
            categories.add(new CategoryWithDisplayName(category));
        }
        RequestLogHandler.handleCorrectResponse(httpRequest);
        return new CorrectResponse(new CategoriesResponseDto(categories), Const.SUCC_ERR, HttpStatus.OK);
    }

    @GetMapping(value = "/getConditions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getConditions(HttpServletRequest httpRequest) {
        RequestLogHandler.handleRequest(httpRequest);
        List<ConditionWithDisplayName> conditionsWithDisplayNames = new ArrayList<>();
        for (Condition condition : Condition.values()) {
            conditionsWithDisplayNames.add(new ConditionWithDisplayName(condition));
        }
        RequestLogHandler.handleCorrectResponse(httpRequest);
        return new CorrectResponse(new ConditionsResponseDto(conditionsWithDisplayNames), Const.SUCC_ERR, HttpStatus.OK);
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> searchOffersForList(HttpServletRequest httpRequest,
                                                      @RequestParam(name = "title", required = false) String title,
                                                      @RequestParam(name = "category", required = false) List<String> category,
                                                      @RequestParam(name = "condition", required = false) String condition,
                                                      @RequestParam(name = "distance", required = false) Double distance,
                                                      @RequestParam(name = "score", required = false) Double score,
                                                      @RequestParam(name = "creationDate", required = false) LocalDate creationDate,
                                                      @RequestParam(name = "sortType", required = false) String sortType
    ) {
        RequestLogHandler.handleRequest(httpRequest);
        UUID userId = identityHandler.getUserIdFromHeader(httpRequest);
        if (Objects.isNull(userId)) {
            return new ErrorResponse("UNAUTHORIZED", HttpStatus.FORBIDDEN);
        }
        List<UUID> foundOfferIdList = searchOffersUseCaseInterface.getOfferListMeetingCriteria(
                userId,
                title,
                category,
                condition,
                distance,
                score,
                creationDate,
                sortType
        );
        RequestLogHandler.handleCorrectResponse(httpRequest);

        return new CorrectResponse(foundOfferIdList, Const.SUCC_ERR, HttpStatus.OK);
    }

    @GetMapping(value = "/generateDescription", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> generateDescription(HttpServletRequest httpRequest, @RequestParam String title, @RequestParam String condition, @RequestParam String category, @RequestParam(required = false) String additionalData) {
        RequestLogHandler.handleRequest(httpRequest);
        DescriptionGenerator generator = new DescriptionGenerator();
        String description;
        try {
            if (additionalData == null) {
                description = generator.generateDescription(title, condition, category, null);
            } else {
                description = generator.generateDescription(title, condition, category, additionalData);
            }
            RequestLogHandler.handleCorrectResponse(httpRequest);
            return new CorrectResponse(description, Const.SUCC_ERR, HttpStatus.OK);
        } catch (IOException | InterruptedException | DescriptionGenerationException e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.INTERNAL_SERVER_ERROR, Const.SERVER_ERR +":  "+ e.getMessage());
            Thread.currentThread().interrupt();
            return new ErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/getNewest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getNewestOffers(HttpServletRequest httpRequest) {
        RequestLogHandler.handleRequest(httpRequest);
        try {
            List<UUID> newestOfferIdList = getNewestOffersUseCaseService.getNewestOffers();
            RequestLogHandler.handleCorrectResponse(httpRequest);
            return new CorrectResponse(newestOfferIdList, Const.SUCC_ERR, HttpStatus.OK);

        } catch (IllegalArgumentException illegalArgumentException) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.NOT_FOUND, illegalArgumentException.getMessage());
            return new ErrorResponse(Const.ILL_ARG_ERR, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/getAllOffers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllOffers(HttpServletRequest httpRequest) {
        RequestLogHandler.handleRequest(httpRequest);
        List<UUID> allOfferIdList = getAllOffersUseCaseInterface.getAllOfferIdList();
        RequestLogHandler.handleCorrectResponse(httpRequest);
        return new CorrectResponse(allOfferIdList, Const.SUCC_ERR, HttpStatus.OK);
    }

    @GetMapping(value = "/getScore/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getUserScore(HttpServletRequest httpRequest, @PathVariable(name = "userId") UUID userId) {
        RequestLogHandler.handleRequest(httpRequest);
        try {
            List<UserScoreWithPositionDto> userScoreWithPositionDtoList = getAllUserListWithScoreAndPosition();
            UserScoreWithPositionDto particularUserRecord = userScoreWithPositionDtoList
                    .stream()
                    .filter(userScoreDto ->userScoreDto.userId().equals(userId))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new);
            RequestLogHandler.handleCorrectResponse(httpRequest);
            return new CorrectResponse(particularUserRecord, Const.SUCC_ERR, HttpStatus.OK);
        }  catch (NoSuchElementException noSuchElementException) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.NOT_FOUND, Const.NO_ELEM_ERR);
            return new ErrorResponse(Const.NO_ELEM_ERR, HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping(value = "/getTopScoreUserList", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getTopScoreUserList(HttpServletRequest httpRequest) {
        RequestLogHandler.handleRequest(httpRequest);
        List<UserScoreWithPositionDto> userScoreWithPositionDtoList = getAllUserListWithScoreAndPosition();
        RequestLogHandler.handleCorrectResponse(httpRequest);
        return new CorrectResponse(userScoreWithPositionDtoList, Const.SUCC_ERR, HttpStatus.OK);

    }

    @GetMapping(value="getReservedOffersByReciever/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getReservedOffersByReciever(HttpServletRequest httpRequest, @PathVariable(name="userId") UUID userId) {
        RequestLogHandler.handleRequest(httpRequest);
        List<UUID> reservedOffersId = getOffersByReceiverAndStatusUseCaseInterface.getReservedOffersByRecieverAndStatus(userId, Status.RESERVED);
        RequestLogHandler.handleCorrectResponse(httpRequest);
        return new CorrectResponse(reservedOffersId, Const.SUCC_ERR, HttpStatus.OK);

    }

    @GetMapping(value="getFinishedOffersByReciever/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getFinishedOffersByReciever(HttpServletRequest httpRequest, @PathVariable(name="userId") UUID userId) {
        RequestLogHandler.handleRequest(httpRequest);
        List<UUID> reservedOffersId = getOffersByReceiverAndStatusUseCaseInterface.getReservedOffersByRecieverAndStatus(userId, Status.FINISHED);
        RequestLogHandler.handleCorrectResponse(httpRequest);
        return new CorrectResponse(reservedOffersId, Const.SUCC_ERR, HttpStatus.OK);

    }

        // ------------------- POST -------------------


    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
                RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Photo could not be added");
                return new ErrorResponse(Const.API_NOT_RESP_ERR + ": Photo could not be added", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.INTERNAL_SERVER_ERROR, "Photo could not be added");
            return new ErrorResponse(Const.API_NOT_RESP_ERR + ": Photo could not be added", HttpStatus.INTERNAL_SERVER_ERROR);

        }

        try {
            getUserProfileUseCaseInterface.getUserProfileResponseDto(offerSaveDto.ownerId());
            OfferSaveResponseDto offerSaveResponseDto = addOfferUseCaseInterface.addOffer(offerSaveDto, photoId);
            RequestLogHandler.handleCorrectResponse(httpRequest);
            return new CorrectResponse(offerSaveResponseDto, Const.SUCC_ERR, HttpStatus.OK);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.BAD_REQUEST, "Validation error");
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (LocationCalculationException e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.BAD_REQUEST, "Location calculation error");
            return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.INTERNAL_SERVER_ERROR, Const.SERVER_ERR +":  "+ e.getMessage());
            Thread.currentThread().interrupt();
            return new ErrorResponse(Const.API_NOT_RESP_ERR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/reserve", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> reserveOffer(HttpServletRequest httpRequest, @RequestBody OfferReserveDto offerReserveDto) {
        RequestLogHandler.handleRequest(httpRequest);
        try {
            if (authenticationHandler.authenticateRequestForUserIdentity(httpRequest, offerReserveDto.receiverId())) {
                UUID offerId = reserveOfferUseCaseInterface.reserveOffer(offerReserveDto);
                RequestLogHandler.handleCorrectResponse(httpRequest);
                return new CorrectResponse(offerId, Const.SUCC_ERR, HttpStatus.OK);
            } else {
                RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.FORBIDDEN, "NO PERMISSIONS");
                return new ErrorResponse(Const.FORBIDDEN_ERR, HttpStatus.FORBIDDEN);
            }
        } catch (NoSuchElementException noSuchElementException) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.NOT_FOUND, Const.NO_ELEM_ERR);
            return new ErrorResponse(Const.NO_ELEM_ERR, HttpStatus.NOT_FOUND);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.FAILED_DEPENDENCY, Const.DATA_INTEGRITY_ERR);
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        }

    }

    @PostMapping(value = "/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> cancelOffer(HttpServletRequest httpRequest, @RequestBody OfferEndDto offerEndDto) {
        RequestLogHandler.handleRequest(httpRequest);
        try {
            if (authenticationHandler.authenticateRequestForUserIdentity(httpRequest, offerEndDto.userId())) {
                UUID offerId = cancelOfferUseCaseInterface.cancelOffer(offerEndDto);
                RequestLogHandler.handleCorrectResponse(httpRequest);
                return new CorrectResponse(offerId, Const.SUCC_ERR, HttpStatus.OK);
            } else {
                RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.FORBIDDEN, Const.FORBIDDEN_ERR);
                return new ErrorResponse(Const.FORBIDDEN_ERR, HttpStatus.FORBIDDEN);
            }
        } catch (NoSuchElementException noSuchElementException) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.NOT_FOUND, Const.NO_ELEM_ERR);
            return new ErrorResponse(Const.NO_ELEM_ERR, HttpStatus.NOT_FOUND);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.FAILED_DEPENDENCY, Const.DATA_INTEGRITY_ERR);
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @PostMapping(value = "/finish", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> finishOffer(HttpServletRequest httpRequest, @RequestBody OfferEndDto offerEndDto) {
        RequestLogHandler.handleRequest(httpRequest);
        try {
            if (authenticationHandler.authenticateRequestForUserIdentity(httpRequest, offerEndDto.userId())) {
                UUID offerId = finishOfferUseCaseInterface.finishOffer(offerEndDto);
                RequestLogHandler.handleCorrectResponse(httpRequest);
                return new CorrectResponse(offerId, Const.SUCC_ERR, HttpStatus.OK);
            } else {
                RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.FORBIDDEN, Const.FORBIDDEN_ERR);
                return new ErrorResponse(Const.FORBIDDEN_ERR, HttpStatus.FORBIDDEN);
            }
        } catch (NoSuchElementException noSuchElementException) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.NOT_FOUND, Const.NO_ELEM_ERR);
            return new ErrorResponse(Const.NO_ELEM_ERR, HttpStatus.NOT_FOUND);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.FAILED_DEPENDENCY, Const.DATA_INTEGRITY_ERR);
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);
        }
    }

    @PostMapping(value = "/addReview", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addReviewToOffer(HttpServletRequest httpRequest, @RequestBody OfferReviewDto offerReviewDto) {
        RequestLogHandler.handleRequest(httpRequest);
        try {
            UUID reviewId = addReviewUseCaseInterface.addReview(offerReviewDto);
            RequestLogHandler.handleCorrectResponse(httpRequest);
            return new CorrectResponse(reviewId, Const.SUCC_ERR, HttpStatus.OK);
        } catch (NoSuchElementException noSuchElementException) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.NOT_FOUND, Const.NO_ELEM_ERR);
            return new ErrorResponse(Const.NO_ELEM_ERR, HttpStatus.NOT_FOUND);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.FAILED_DEPENDENCY, Const.DATA_INTEGRITY_ERR);
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.FAILED_DEPENDENCY);

        }
    }

    // ------------------- PUT -------------------


    @PutMapping(value = "/modify/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> modifyOffer(HttpServletRequest httpRequest, @PathVariable(value = "id") UUID offerId, @RequestBody OfferModifyDto offerModifyDto) {
        RequestLogHandler.handleRequest(httpRequest);
        try {
            modifyOfferUseCaseInterface.modifyOffer(offerId, offerModifyDto);
            RequestLogHandler.handleCorrectResponse(httpRequest);
            return new CorrectResponse(offerId, Const.SUCC_ERR, HttpStatus.OK);
        } catch (NoSuchElementException noSuchElementException) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.NOT_FOUND, Const.NO_ELEM_ERR);
            return new ErrorResponse(Const.NO_ELEM_ERR, HttpStatus.NOT_FOUND);
        } catch (MultipleValidationException e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.BAD_REQUEST, "Validation error");
            return new ErrorResponse(e.getErrorMap(), e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (LocationCalculationException | IOException | InterruptedException e) {
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.BAD_REQUEST, "Location calculation error");
            Thread.currentThread().interrupt();
            return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    private ShareioResponse getOfferListBasedOnStatus(HttpServletRequest httpRequest, UUID id, Status status) {
        RequestLogHandler.handleRequest(httpRequest);
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("offerIds", getOffersByUserAndStatusUseCaseInterface.getOfferResponseDtoListByUser(id, status));
            RequestLogHandler.handleCorrectResponse(httpRequest);
            return new CorrectResponse(response, Const.SUCC_ERR, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            response.put("error", e.getMessage());
            RequestLogHandler.handleErrorResponse(httpRequest, HttpStatus.NOT_FOUND, Const.NO_ELEM_ERR);
            return new ErrorResponse(Const.NO_ELEM_ERR, HttpStatus.NOT_FOUND);
        }
    }

    private List<UserScoreWithPositionDto> getAllUserListWithScoreAndPosition(){
        List<UUID> userIdList = getAllUserIdListUseCaseInterface.getAllUserIdList();
        List<UserScoreDto> userScoreDtoList = new ArrayList<>();
        List<UserScoreDto> finalUserScoreDtoList = userScoreDtoList;
        userIdList.forEach(userId -> {
            UserProfileResponseDto userProfileResponseDto;
            try {
                userProfileResponseDto = getUserProfileUseCaseInterface.getUserProfileResponseDto(userId);
            } catch (MultipleValidationException e) {
                return;
            }
            finalUserScoreDtoList.add(new UserScoreDto(
                    userId,
                    userProfileResponseDto.name()+" "+userProfileResponseDto.surname(),
                    getAverageUserReviewValueUseCaseInterface.getAverageUserReviewValue(userId)
            ));
        });

        userScoreDtoList = finalUserScoreDtoList
                .stream()
                .sorted(Comparator.comparing(UserScoreDto::score))
                .toList()
                .reversed();

        List<UserScoreWithPositionDto> userScoreWithPositionDtoList = new ArrayList<>();
        List<UserScoreDto> finalLambdaUserScoreDtoList = userScoreDtoList;
        userScoreDtoList.forEach(userScoreDto -> userScoreWithPositionDtoList.add(new UserScoreWithPositionDto(
                userScoreDto.userId(),
                userScoreDto.nameAndSurname(),
                userScoreDto.score(),
                finalLambdaUserScoreDtoList.indexOf(userScoreDto)+1))
        );
        return userScoreWithPositionDtoList;
    }

}