package com.levelup.jmsinfo.cmmn.code;

import lombok.Getter;

/**
 * @purpose [공통 코드] API ?��?��?�� ???�� '?��?�� 코드'�? Enum ?��?���? �?리�?? ?��?��.
 * Global Error CodeList : ?��?��?���? 발생?��?�� ?��?��코드�? �?리한?��.
 * Custom Error CodeList : ?���? ?��?���??��?�� 발생?��?�� ?��?��코드�? �?리한?��
 * Error Code Constructor : ?��?��코드�? 직접?��?���? ?��?��?���? ?��?�� ?��?��?���? 구성?��?��.
 *
 * @  ?��?��?��            ?��?��?��       ?��?��?��?��
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ?��민서       최초?��?��
 *
 * @author ?��민서
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
    // ?��못된 ?���? ?���?
    BAD_REQUEST_ERROR(400, "G001", "Bad Request Exception"),

    // @RequestBody ?��?��?�� �? 존재
    REQUEST_BODY_MISSING_ERROR(400, "G002", "Required request body is missing"),

    // ?��?��?���? ?��?? ???��
    INVALID_TYPE_VALUE(400, "G003", " Invalid Type Value"),

    // Request Parameter �? ?��?��?���? ?��?��?���? ?��?�� 경우
    MISSING_REQUEST_PARAMETER_ERROR(400, "G004", "Missing Servlet RequestParameter Exception"),

    // ?��?��/출력 값이 ?��?��?���? ?��?��
    IO_ERROR(400, "G005", "I/O Exception"),

    // com.google.gson JSON ?��?�� ?��?��
    JSON_PARSE_ERROR(400, "G006", "JsonParseException"),

    // com.fasterxml.jackson.core Processing Error
    JACKSON_PROCESS_ERROR(400, "G007", "com.fasterxml.jackson.core Exception"),

    // 권한?�� ?��?��
    FORBIDDEN_ERROR(403, "G008", "Forbidden Exception"),

    // ?��버로 ?���??�� 리소?���? 존재?���? ?��?��
    NOT_FOUND_ERROR(404, "G009", "Not Found Exception"),

    // NULL Point Exception 발생
    NULL_POINT_ERROR(404, "G010", "Null Point Exception"),

    // @RequestBody �? @RequestParam, @PathVariable 값이 ?��?��?���? ?��?��
    NOT_VALID_ERROR(404, "G011", "handle Validation Exception"),

    // @RequestBody �? @RequestParam, @PathVariable 값이 ?��?��?���? ?��?��
    NOT_VALID_HEADER_ERROR(404, "G012", "Header?�� ?��?��?���? 존재?���? ?��?�� 경우 "),

    // ?��버�? 처리 ?�� 방법?�� 모르?�� 경우 발생
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
    // ?��?�� 코드?�� '코드 ?��?��'?�� 반환?��?��.
    private final int status;

    // ?��?�� 코드?�� '코드�? 구분 �?'?�� 반환?��?��.
    private final String divisionCode;

    // ?��?�� 코드?�� '코드 메시�?'?�� 반환?��?��.
    private final String message;

    // ?��?��?�� 구성
    ErrorCode(final int status, final String divisionCode, final String message) {
        this.status = status;
        this.divisionCode = divisionCode;
        this.message = message;
    }
}
