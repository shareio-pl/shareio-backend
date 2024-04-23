package org.shareio.backend.validators;

import org.shareio.backend.exceptions.ValidationException;

import java.util.Objects;

public class DoubleValidator {
    public static void validateDoubleNotEmpty(Double d) throws ValidationException {
        if (Objects.isNull(d))
            throw new ValidationException("Double is empty");
    }

    public static void validateDoubleBounds(Double d, Double min, Double max) throws ValidationException {
        if (d < min) throw new ValidationException("Value too low");
        if (d > max) throw new ValidationException("Value too high");
    }
}
