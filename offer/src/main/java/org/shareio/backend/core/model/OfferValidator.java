package org.shareio.backend.core.model;

import org.shareio.backend.Const;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.exceptions.ValidationException;
import org.shareio.backend.validators.DoubleValidator;
import org.shareio.backend.validators.IntegerValidator;
import org.shareio.backend.validators.ObjectValidator;
import org.shareio.backend.validators.StringValidator;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.shareio.backend.core.model.AddressValidator.*;
import static org.shareio.backend.core.model.UserValidator.validateName;

public class OfferValidator {
    public static void validateOffer(OfferGetDto offerGetDto) throws MultipleValidationException {
        Map<String, String> errorMap = new HashMap<>();
        try {
            validateDate(offerGetDto.creationDate());
        } catch (ValidationException validationException) {
            errorMap.put("CreationDate", validationException.getMessage());
        }
        try {
            validateStatus(offerGetDto.status());
        } catch (ValidationException validationException) {
            errorMap.put("Status", validationException.getMessage());
        }
        try {
            validateCity(offerGetDto.city());
        } catch (ValidationException validationException) {
            errorMap.put("City", validationException.getMessage());
        }
        try {
            validateStreet(offerGetDto.street());
        } catch (ValidationException validationException) {
            errorMap.put("Street", validationException.getMessage());
        }
        try {
            validateHouseNumber(offerGetDto.houseNumber());
        } catch (ValidationException validationException) {
            errorMap.put("HouseNumber", validationException.getMessage());
        }
        try {
            validateLatLon(offerGetDto.latitude());
        } catch (ValidationException validationException) {
            errorMap.put("Latitude", validationException.getMessage());
        }
        try {
            validateLatLon(offerGetDto.longitude());
        } catch (ValidationException validationException) {
            errorMap.put("Longitude", validationException.getMessage());
        }

        try {
            validateTitle(offerGetDto.title());
        } catch (ValidationException validationException) {
            errorMap.put("Title", validationException.getMessage());
        }
        try {
            validateCondition(offerGetDto.condition());
        } catch (ValidationException validationException) {
            errorMap.put("Condition", validationException.getMessage());
        }
        try {
            validateDescription(offerGetDto.description());
        } catch (ValidationException validationException) {
            errorMap.put("Description", validationException.getMessage());
        }

        try {
            validateName(offerGetDto.ownerName());
        } catch (ValidationException validationException) {
            errorMap.put("Name", validationException.getMessage());
        }
        try {
            validateName(offerGetDto.ownerSurname());
        } catch (ValidationException validationException) {
            errorMap.put("Surname", validationException.getMessage());
        }
        try {
            validateOwnerRating(offerGetDto.ownerRating());
        } catch (ValidationException validationException) {
            errorMap.put("OwnerRating", validationException.getMessage());
        }
        try {
            validateOwnerReviewCount(offerGetDto.ownerReviewCount());
        } catch (ValidationException validationException) {
            errorMap.put("OwnerReviewCount", validationException.getMessage());
        }
        if (!errorMap.isEmpty()) throw new MultipleValidationException(Const.multipleValidationErrorCode, errorMap);
    }

    public static void validateDate(LocalDateTime date) throws ValidationException {
        ObjectValidator.validateObjectIsNotNull(date);
    }

    public static void validateStatus(String status) throws ValidationException {
        ObjectValidator.validateObjectIsNotNull(status);
        try {
            Status.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Bad status");
        }
    }

    public static void validateTitle(String title) throws ValidationException {
        ObjectValidator.validateObjectIsNotNull(title);
        StringValidator.validateStringNotEmpty(title);
        StringValidator.validateStringLength(title, Const.minTitleLength, Const.maxTitleLength);
    }

    public static void validateCondition(String condition) throws ValidationException {
        ObjectValidator.validateObjectIsNotNull(condition);
        try {
            Condition.valueOf(condition);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Bad condition");
        }
    }

    public static void validateDescription(String description) throws ValidationException {
        ObjectValidator.validateObjectIsNotNull(description);
        StringValidator.validateStringNotEmpty(description);
        StringValidator.validateStringLength(description, Const.minDescriptionLength, Const.maxDescriptionLength);
    }

    public static void validateOwnerRating(Double ownerRating) throws ValidationException {
        DoubleValidator.validateDoubleNotEmpty(ownerRating);
        DoubleValidator.validateDoubleBounds(ownerRating, 0.0, 5.0);
    }

    public static void validateOwnerReviewCount(Integer ownerReviewCount) throws ValidationException {
        IntegerValidator.validateIntegerNotEmpty(ownerReviewCount);
        IntegerValidator.validateIntegerNotNegative(ownerReviewCount);
    }
}
