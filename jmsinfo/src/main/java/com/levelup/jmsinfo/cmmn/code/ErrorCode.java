package com.levelup.jmsinfo.cmmn.code;

import lombok.Getter;

/**
 * @purpose [ê³µí†µ ì½”ë“œ] API ?†µ?‹ ?— ???•œ '?—?Ÿ¬ ì½”ë“œ'ë¥? Enum ?˜•?ƒœë¡? ê´?ë¦¬ë?? ?•œ?‹¤.
 * Global Error CodeList : ? „?—­?œ¼ë¡? ë°œìƒ?•˜?Š” ?—?Ÿ¬ì½”ë“œë¥? ê´?ë¦¬í•œ?‹¤.
 * Custom Error CodeList : ?—…ë¬? ?˜?´ì§??—?„œ ë°œìƒ?•˜?Š” ?—?Ÿ¬ì½”ë“œë¥? ê´?ë¦¬í•œ?‹¤
 * Error Code Constructor : ?—?Ÿ¬ì½”ë“œë¥? ì§ì ‘? ?œ¼ë¡? ?‚¬?š©?•˜ê¸? ?œ„?•œ ?ƒ?„±?ë¥? êµ¬ì„±?•œ?‹¤.
 *
 * @  ?ˆ˜? •?¼            ?ˆ˜? •?       ?ˆ˜? •?‚´?š©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ? •ë¯¼ì„œ       ìµœì´ˆ?ƒ?„±
 *
 * @author ? •ë¯¼ì„œ
 * @since  2023.02.08
 *
 */
@Getter
public enum ErrorCode {

    /**
     * ******************************* Global Error CodeList ***************************************
     * HTTP Status Code
     * 400 : Bad Request
     * 401 : Unauthorized
     * 403 : Forbidden
     * 404 : Not Found
     * 500 : Internal Server Error
     * *********************************************************************************************
     */
    // ?˜ëª»ëœ ?„œë²? ?š”ì²?
    BAD_REQUEST_ERROR(400, "G001", "Bad Request Exception"),

    // @RequestBody ?°?´?„° ë¯? ì¡´ì¬
    REQUEST_BODY_MISSING_ERROR(400, "G002", "Required request body is missing"),

    // ?œ ?š¨?•˜ì§? ?•Š?? ???…
    INVALID_TYPE_VALUE(400, "G003", " Invalid Type Value"),

    // Request Parameter ë¡? ?°?´?„°ê°? ? „?‹¬?˜ì§? ?•Š?„ ê²½ìš°
    MISSING_REQUEST_PARAMETER_ERROR(400, "G004", "Missing Servlet RequestParameter Exception"),

    // ?…? ¥/ì¶œë ¥ ê°’ì´ ?œ ?š¨?•˜ì§? ?•Š?Œ
    IO_ERROR(400, "G005", "I/O Exception"),

    // com.google.gson JSON ?ŒŒ?‹± ?‹¤?Œ¨
    JSON_PARSE_ERROR(400, "G006", "JsonParseException"),

    // com.fasterxml.jackson.core Processing Error
    JACKSON_PROCESS_ERROR(400, "G007", "com.fasterxml.jackson.core Exception"),

    // ê¶Œí•œ?´ ?—†?Œ
    FORBIDDEN_ERROR(403, "G008", "Forbidden Exception"),

    // ?„œë²„ë¡œ ?š”ì²??•œ ë¦¬ì†Œ?Š¤ê°? ì¡´ì¬?•˜ì§? ?•Š?Œ
    NOT_FOUND_ERROR(404, "G009", "Not Found Exception"),

    // NULL Point Exception ë°œìƒ
    NULL_POINT_ERROR(404, "G010", "Null Point Exception"),

    // @RequestBody ë°? @RequestParam, @PathVariable ê°’ì´ ?œ ?š¨?•˜ì§? ?•Š?Œ
    NOT_VALID_ERROR(404, "G011", "handle Validation Exception"),

    // @RequestBody ë°? @RequestParam, @PathVariable ê°’ì´ ?œ ?š¨?•˜ì§? ?•Š?Œ
    NOT_VALID_HEADER_ERROR(404, "G012", "Header?— ?°?´?„°ê°? ì¡´ì¬?•˜ì§? ?•Š?Š” ê²½ìš° "),

    // ?„œë²„ê? ì²˜ë¦¬ ?•  ë°©ë²•?„ ëª¨ë¥´?Š” ê²½ìš° ë°œìƒ
    INTERNAL_SERVER_ERROR(500, "G999", "Internal Server Error Exception"),


    /**
     * ******************************* Custom Error CodeList ***************************************
     */
    // Transaction Insert Error
    INSERT_ERROR(200, "9999", "Insert Transaction Error Exception"),

    // Transaction Update Error
    UPDATE_ERROR(200, "9999", "Update Transaction Error Exception"),

    // Transaction Delete Error
    DELETE_ERROR(200, "9999", "Delete Transaction Error Exception"),
    
    BUSINESS_EXCEPTION_ERROR(200, "9999", "Business Exception"),
    
    ACCESS_TOKEN_ERROR(200, "JWT01", "Access Token Invalid"),
    
    RELOGIN_TOKEN_ERROR(200, "LOGIN", "Please Re-Login")
    
    ; // End

    /**
     * ******************************* Error Code Constructor ***************************************
     */
    // ?—?Ÿ¬ ì½”ë“œ?˜ 'ì½”ë“œ ?ƒ?ƒœ'?„ ë°˜í™˜?•œ?‹¤.
    private final int status;

    // ?—?Ÿ¬ ì½”ë“œ?˜ 'ì½”ë“œê°? êµ¬ë¶„ ê°?'?„ ë°˜í™˜?•œ?‹¤.
    private final String divisionCode;

    // ?—?Ÿ¬ ì½”ë“œ?˜ 'ì½”ë“œ ë©”ì‹œì§?'?„ ë°˜í™˜?•œ?‹¤.
    private final String message;

    // ?ƒ?„±? êµ¬ì„±
    ErrorCode(final int status, final String divisionCode, final String message) {
        this.status = status;
        this.divisionCode = divisionCode;
        this.message = message;
    }
}
