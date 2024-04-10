package org.shareio.backend.core.model;

import org.shareio.backend.Const;
import org.shareio.backend.core.usecases.port.dto.AddressGetDto;
import org.shareio.backend.core.usecases.port.dto.LocationGetDto;
import org.shareio.backend.exceptions.MultipleValidationException;
import org.shareio.backend.exceptions.ValidationException;
import org.shareio.backend.validators.DoubleValidator;
import org.shareio.backend.validators.StringValidator;

import java.util.HashMap;
import java.util.Map;

public class AddressValidator {
    public static void validateAddress(AddressGetDto addressGetDto) throws MultipleValidationException {
        Map<String, String> errorMap = new HashMap<>();
        try {
            validateCountry(addressGetDto.country());
        } catch (ValidationException validationException) {
            errorMap.put("Country", validationException.getMessage());
        }
        try {
            validateRegion(addressGetDto.region());
        } catch (ValidationException validationException) {
            errorMap.put("Region", validationException.getMessage());
        }
        try {
            validateCity(addressGetDto.city());
        } catch (ValidationException validationException) {
            errorMap.put("City", validationException.getMessage());
        }
        try {
            validateStreet(addressGetDto.street());
        } catch (ValidationException validationException) {
            errorMap.put("Street", validationException.getMessage());
        }
        try {
            validateHouseNumber(addressGetDto.houseNumber());
        } catch (ValidationException validationException) {
            errorMap.put("HouseNumber", validationException.getMessage());
        }
        try {
            validateFlatNumber(addressGetDto.flatNumber());
        } catch (ValidationException validationException) {
            errorMap.put("FlatNumber", validationException.getMessage());
        }
        try {
            validatePostCode(addressGetDto.postCode());
        } catch (ValidationException validationException) {
            errorMap.put("PostCode", validationException.getMessage());
        }
        if (!errorMap.isEmpty()) throw new MultipleValidationException(Const.multipleValidationErrorCode, errorMap);
    }

    public static void validateLocation(LocationGetDto locationGetDto) throws MultipleValidationException {
        Map<String, String> errorMap = new HashMap<>();
        try {
            validateLatLon(locationGetDto.latitude());
        } catch (ValidationException validationException) {
            errorMap.put("Latitude", validationException.getMessage());
        }
        try {
            validateLatLon(locationGetDto.longitude());
        } catch (ValidationException validationException) {
            errorMap.put("Longitude", validationException.getMessage());
        }
        if (!errorMap.isEmpty()) throw new MultipleValidationException(Const.multipleValidationErrorCode, errorMap);
    }

    public static void validateCountry(String country) throws ValidationException {
        StringValidator.validateStringNotEmpty(country);
        StringValidator.validateStringOnlyLettersOrNumbersOrSpacesOrSomeSpecialCharacters(country);
    }

    public static void validateRegion(String region) throws ValidationException {
        StringValidator.validateStringNotEmpty(region);
        StringValidator.validateStringOnlyLettersOrNumbersOrSpacesOrSomeSpecialCharacters(region);
    }

    public static void validateCity(String city) throws ValidationException {
        StringValidator.validateStringNotEmpty(city);
        StringValidator.validateStringOnlyLettersOrNumbersOrSpacesOrSomeSpecialCharacters(city);
    }

    public static void validateStreet(String street) throws ValidationException {
        StringValidator.validateStringNotEmpty(street);
        StringValidator.validateStringOnlyLettersOrNumbersOrSpacesOrSomeSpecialCharacters(street);
    }

    public static void validateHouseNumber(String houseNumber) throws ValidationException {
        StringValidator.validateStringNotEmpty(houseNumber);
        StringValidator.validateStringOnlyLettersOrNumbersOrSlashes(houseNumber);
    }

    public static void validateFlatNumber(String flatNumber) throws ValidationException {
        StringValidator.validateStringNotEmpty(flatNumber);
        StringValidator.validateStringOnlyLettersOrNumbersOrSlashesOrDots(flatNumber);
    }

    public static void validatePostCode(String postCode) throws ValidationException {
        try {
            StringValidator.validateStringNotEmpty(postCode);
        } catch (ValidationException validationException) {
            return;
        }
        StringValidator.validatePolishPostCode(postCode);
    }

    public static void validateLatLon(Double latOrLon) throws ValidationException {
        DoubleValidator.validateDoubleNotEmpty(latOrLon);
    }
}
