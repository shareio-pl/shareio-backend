package org.shareio.backend;

import java.time.Duration;
import java.util.UUID;
import java.util.regex.Pattern;

public class Const {
    // -------------- CONFIGURATION ---------------
    public static final Duration offerReservationDuration = Duration.ofDays(1);
    public static final Double defaultAddressCenterLat = 0.0;
    public static final Double defaultAddressCenterLon = 0.0;

    // ---------------- VALIDATION ----------------
    public static final Integer minNameLength = 2;
    public static final Integer maxNameLength = 20;
    public static final Integer minTitleLength = 4;
    public static final Integer maxTitleLength = 30;
    public static final Integer minDescriptionLength = 20;
    public static final Integer maxDescriptionLength = 450;

    // ------------- VALIDATION REGEX -------------
    // borrowed from https://emailregex.com/index.html
    public static final String emailRegexString = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])$";
    public static final Pattern emailRegex = Pattern.compile(emailRegexString, Pattern.CASE_INSENSITIVE);
    public static final String polishPostCodeRegexString = "^\\d\\d-\\d\\d\\d$";
    public static final Pattern polishPostCodeRegex = Pattern.compile(polishPostCodeRegexString, Pattern.CASE_INSENSITIVE);

    // ------------------ ERRORS ------------------
    public static final String successErrorCode = "Ok";
    public static final String notImplementedErrorCode = "NoImplErr";
    public static final String multipleValidationErrorCode = "MulValErr";
    public static final String noSuchElementErrorCode = "NoElemErr";
    public static final String toDoErrorCode = "TODOErr";
    public static final String illegalArgumentErrorCode = "IllArgErr";
    public static final String cannotDetermineAddressErrorCode = "CantDetAddrErr";
    public static final String APINotRespondingErrorCode = "ApiSięWyjebałoNaGupiRyj";

    // ----------------- DEFAULTS ------------------

    public static final UUID defaultPhotoId = UUID.fromString("94416390-3990-48f3-b9be-de29fa446c04");

}
