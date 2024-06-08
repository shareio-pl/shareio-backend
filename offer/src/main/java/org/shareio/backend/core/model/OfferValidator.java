package org.shareio.backend.core.model;

import org.shareio.backend.Const;
import org.shareio.backend.core.model.vo.Category;
import org.shareio.backend.core.model.vo.Condition;
import org.shareio.backend.core.model.vo.Status;
import org.shareio.backend.core.usecases.port.dto.OfferGetDto;
import org.shareio.backend.core.usecases.port.dto.OfferSaveDto;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.exceptions.ValidationException;
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
            validateCategory(offerGetDto.category());
        } catch (ValidationException validationException) {
            errorMap.put("Category", validationException.getMessage());
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
        if (!errorMap.isEmpty()) throw new MultipleValidationException(Const.MUL_VAL_ERR, errorMap);
    }

    public static void validateOffer(OfferSaveDto offerSaveDto) throws MultipleValidationException {
        Map<String, String> errorMap = new HashMap<>();
        try {
            validateCity(offerSaveDto.city());
        } catch (ValidationException validationException) {
            errorMap.put("City", validationException.getMessage());
        }
        try {
            validateStreet(offerSaveDto.street());
        } catch (ValidationException validationException) {
            errorMap.put("Street", validationException.getMessage());
        }
        try {
            validateHouseNumber(offerSaveDto.houseNumber());
        } catch (ValidationException validationException) {
            errorMap.put("HouseNumber", validationException.getMessage());
        }

        try {
            validateTitle(offerSaveDto.title());
        } catch (ValidationException validationException) {
            errorMap.put("Title", validationException.getMessage());
        }
        try {
            validateCondition(offerSaveDto.condition());
        } catch (ValidationException validationException) {
            errorMap.put("Condition", validationException.getMessage());
        }
        try {
            validateCategory(offerSaveDto.category());
        } catch (ValidationException validationException) {
            errorMap.put("Category", validationException.getMessage());
        }
        try {
            validateDescription(offerSaveDto.description());
        } catch (ValidationException validationException) {
            errorMap.put("Description", validationException.getMessage());
        }
        if (!errorMap.isEmpty()) throw new MultipleValidationException(Const.MUL_VAL_ERR, errorMap);
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
        StringValidator.validateStringLength(title, Const.MIN_TITLE_LENGTH, Const.MAX_TITLE_LENGTH);
    }


    public static void validateCondition(String condition) throws ValidationException {
        ObjectValidator.validateObjectIsNotNull(condition);
        try {
            Condition.valueOf(condition);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Bad condition");
        }
    }

    public static void validateCategory(String category) throws ValidationException {
        ObjectValidator.validateObjectIsNotNull(category);
        try {
            Category.valueOf(category);
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Bad category!");
        }
    }

    public static void validateDescription(String description) throws ValidationException {
        ObjectValidator.validateObjectIsNotNull(description);
        StringValidator.validateStringNotEmpty(description);
        StringValidator.validateStringLength(description, Const.MIN_DESCRIPTION_LENGTH, Const.MAX_DESCRIPTION_LENGTH);
    }

    private OfferValidator() {
        throw new IllegalArgumentException("Utility class");
    }

}
