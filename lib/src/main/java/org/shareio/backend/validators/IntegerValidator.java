package org.shareio.backend.validators;

import org.shareio.backend.exceptions.ValidationException;

import java.util.Objects;

public class IntegerValidator {
    public static void validateIntegerNotEmpty(Integer i) throws ValidationException {
        if (Objects.isNull(i))
            throw new ValidationException("Integer is empty");
    }

    public static void validateIntegerNotNegative(Integer i) throws ValidationException {
        if (i < 0) throw new ValidationException("Negative value");
    }

    public static void validateIntegerBounds(Integer i, Integer min, Integer max) throws ValidationException {
        if (i < min) throw new ValidationException("Value too low");
        if (i > max) throw new ValidationException("Value too high");
    }
}
