package com.levelup.jmsinfo.cmmn.code;

import lombok.Getter;

/**
 * @purpose [κ³΅ν΅ μ½λ] API ?΅? ? ??? '??¬ μ½λ'λ₯? Enum ??λ‘? κ΄?λ¦¬λ?? ??€.
 * Global Error CodeList : ? ?­?Όλ‘? λ°μ?? ??¬μ½λλ₯? κ΄?λ¦¬ν?€.
 * Custom Error CodeList : ?λ¬? ??΄μ§??? λ°μ?? ??¬μ½λλ₯? κ΄?λ¦¬ν?€
 * Error Code Constructor : ??¬μ½λλ₯? μ§μ ? ?Όλ‘? ?¬?©?κΈ? ?? ??±?λ₯? κ΅¬μ±??€.
 *
 * @  ?? ?Ό            ?? ?       ?? ?΄?©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ? λ―Όμ       μ΅μ΄??±
 *
 * @author ? λ―Όμ
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
    // ?λͺ»λ ?λ²? ?μ²?
    BAD_REQUEST_ERROR(400, "G001", "Bad Request Exception"),

    // @RequestBody ?°?΄?° λ―? μ‘΄μ¬
    REQUEST_BODY_MISSING_ERROR(400, "G002", "Required request body is missing"),

    // ? ?¨?μ§? ??? ???
    INVALID_TYPE_VALUE(400, "G003", " Invalid Type Value"),

    // Request Parameter λ‘? ?°?΄?°κ°? ? ?¬?μ§? ?? κ²½μ°
    MISSING_REQUEST_PARAMETER_ERROR(400, "G004", "Missing Servlet RequestParameter Exception"),

    // ?? ₯/μΆλ ₯ κ°μ΄ ? ?¨?μ§? ??
    IO_ERROR(400, "G005", "I/O Exception"),

    // com.google.gson JSON ??± ?€?¨
    JSON_PARSE_ERROR(400, "G006", "JsonParseException"),

    // com.fasterxml.jackson.core Processing Error
    JACKSON_PROCESS_ERROR(400, "G007", "com.fasterxml.jackson.core Exception"),

    // κΆν?΄ ??
    FORBIDDEN_ERROR(403, "G008", "Forbidden Exception"),

    // ?λ²λ‘ ?μ²?? λ¦¬μ?€κ°? μ‘΄μ¬?μ§? ??
    NOT_FOUND_ERROR(404, "G009", "Not Found Exception"),

    // NULL Point Exception λ°μ
    NULL_POINT_ERROR(404, "G010", "Null Point Exception"),

    // @RequestBody λ°? @RequestParam, @PathVariable κ°μ΄ ? ?¨?μ§? ??
    NOT_VALID_ERROR(404, "G011", "handle Validation Exception"),

    // @RequestBody λ°? @RequestParam, @PathVariable κ°μ΄ ? ?¨?μ§? ??
    NOT_VALID_HEADER_ERROR(404, "G012", "Header? ?°?΄?°κ°? μ‘΄μ¬?μ§? ?? κ²½μ° "),

    // ?λ²κ? μ²λ¦¬ ?  λ°©λ²? λͺ¨λ₯΄? κ²½μ° λ°μ
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
    // ??¬ μ½λ? 'μ½λ ??'? λ°ν??€.
    private final int status;

    // ??¬ μ½λ? 'μ½λκ°? κ΅¬λΆ κ°?'? λ°ν??€.
    private final String divisionCode;

    // ??¬ μ½λ? 'μ½λ λ©μμ§?'? λ°ν??€.
    private final String message;

    // ??±? κ΅¬μ±
    ErrorCode(final int status, final String divisionCode, final String message) {
        this.status = status;
        this.divisionCode = divisionCode;
        this.message = message;
    }
}
