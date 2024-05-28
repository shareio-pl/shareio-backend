package org.shareio.backend;

import java.time.Duration;
import java.util.UUID;
import java.util.regex.Pattern;

public class Const {
    // -------------- CONFIGURATION ---------------
    public static final Duration OFFER_RESERVATION_DURATION = Duration.ofDays(1);
    public static final int OFFER_RESERVATION_CHECK_RATE = 1000*60*5;
    public static final Double DEFAULT_ADDRESS_CENTER_LAT = 0.0;
    public static final Double DEFAULT_ADDRESS_CENTER_LON = 0.0;

    // ---------------- VALIDATION ----------------
    public static final Integer MIN_NAME_LENGTH = 2;
    public static final Integer MAX_NAME_LENGTH = 20;
    public static final Integer MIN_TITLE_LENGTH = 4;
    public static final Integer MAX_TITLE_LENGTH = 30;
    public static final Integer MIN_DESCRIPTION_LENGTH = 20;
    public static final Integer MAX_DESCRIPTION_LENGTH = 450;

    // ------------- VALIDATION REGEX -------------
    // borrowed from https://emailregex.com/index.html
    public static final String EMAIL_REGEX_STRING = "^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])$"; // NOSONAR
    public static final Pattern EMAIL_REGEX = Pattern.compile(EMAIL_REGEX_STRING, Pattern.CASE_INSENSITIVE);
    public static final String POLISH_POST_CODE_REGEX_STRING = "^\\d\\d-\\d\\d\\d$";
    public static final Pattern POLISH_POST_CODE_REGEX = Pattern.compile(POLISH_POST_CODE_REGEX_STRING, Pattern.CASE_INSENSITIVE);

    // ------------------ ERRORS ------------------
    public static final String SUCC_ERR = "Ok";
    public static final String NO_IMPL_ERR = "NoImplErr";
    public static final String MUL_VAL_ERR = "MulValErr";
    public static final String NO_ELEM_ERR = "NoElemErr";
    public static final String TO_DO_ERR = "TODOErr";
    public static final String ILL_ARG_ERR = "IllArgErr";
    public static final String CANT_DET_ADDR_ERR = "CantDetAddrErr";
    public static final String API_NOT_RESP_ERR = "ApiNotResp";
    public static final String UNSUPP_MEDIA_TYPE_ERR = "UnsuppMediaTypeErr";
    public static final String DATA_INTEGRITY_ERR = "DataIntegrityErr";
    public static final String SERVER_ERR = "ServerErr";


    // ----------------- DEFAULTS ------------------

    public static final UUID DEFAULT_PHOTO_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final Integer MIN_OFFER_LIST_SIZE = 2;

    // ----------------- EMAIL ---------------------

    public static final String MESSAGE_START = "Zgłoszony problem:\n";
    public static final String MESSAGE_END = "\nDziękujemy za skorzystanie z naszego panelu pomocy technicznej! \nZespół Shareio";

    private Const(){
        throw new IllegalArgumentException("Utility class");
    }
}
