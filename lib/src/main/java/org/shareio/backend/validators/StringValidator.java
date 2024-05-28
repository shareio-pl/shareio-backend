package org.shareio.backend.validators;

import org.shareio.backend.Const;
import org.shareio.backend.exceptions.ValidationException;

import java.util.Objects;

public class StringValidator {
    
    private static final String NON_LETTER_STRING = "String contains non letter characters";
    public static void validateStringNotEmpty(String string) throws ValidationException {
        if (Objects.isNull(string) || string.isBlank() || string.isEmpty())
            throw new ValidationException("String is empty");
    }

    public static void validateStringLength(String string, Integer min, Integer max) throws ValidationException {
        if (string.length() < min) throw new ValidationException("String too short");
        if (max > 0 && string.length() > max) throw new ValidationException("String too long");
    }

    public static void validateStringOnlyLetters(String string) throws ValidationException {
        char[] chars = string.toCharArray();

        for (char c : chars) {
            if (!Character.isLetter(c)) {
                throw new ValidationException(NON_LETTER_STRING);
            }
        }
    }

    public static void validateStringOnlyLettersOrNumbers(String string) throws ValidationException {
        char[] chars = string.toCharArray();

        for (char c : chars) {
            if (!Character.isLetterOrDigit(c)) {
                throw new ValidationException(NON_LETTER_STRING);
            }
        }
    }

    public static void validateStringOnlyLettersOrNumbersOrSlashes(String string) throws ValidationException {
        char[] chars = string.toCharArray();

        for (char c : chars) {
            if (!Character.isLetterOrDigit(c) && (c != '/')) {
                throw new ValidationException(NON_LETTER_STRING);
            }
        }
    }

    public static void validateStringOnlyLettersOrNumbersOrSlashesOrDots(String string) throws ValidationException {
        char[] chars = string.toCharArray();

        for (char c : chars) {
            if (!Character.isLetterOrDigit(c) && (c != '/') && (c != '.')) {
                throw new ValidationException(NON_LETTER_STRING);
            }
        }
    }

    public static void validateStringOnlyLettersOrNumbersOrSpacesOrSomeSpecialCharacters(String string) throws ValidationException {
        char[] chars = string.toCharArray();

        for (char c : chars) {
            if (!Character.isLetterOrDigit(c) && !Character.isSpaceChar(c) && (-1 != "-.".indexOf(c))) {
                throw new ValidationException("String contains not ok character");
            }
        }
    }

    public static void validatePolishPostCode(String polishPostCode) throws ValidationException {
        if (!Const.POLISH_POST_CODE_REGEX.matcher(polishPostCode).find())
            throw new ValidationException("Invalid polish post code");
    }

    public static void validateEmail(String email) throws ValidationException {
        if (!Const.EMAIL_REGEX.matcher(email).find())
            throw new ValidationException("Invalid email");
    }
    
    private StringValidator() {
        throw new IllegalArgumentException("Utility class");
    }
}
