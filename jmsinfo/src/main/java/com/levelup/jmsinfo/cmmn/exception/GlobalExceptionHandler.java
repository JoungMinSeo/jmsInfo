package com.levelup.jmsinfo.cmmn.exception;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonParseException;
import com.levelup.jmsinfo.cmmn.code.ErrorCode;
import com.levelup.jmsinfo.cmmn.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @purpose Controller ?Ç¥?óê?Ñú Î∞úÏÉù?ïò?äî Exception ???ï¥?Ñú Catch ?ïò?ó¨ ?ùë?ãµÍ∞?(Response)?ùÑ Î≥¥ÎÇ¥Ï£ºÎäî Í∏∞Îä•?ùÑ ?àò?ñâ?ï®.
 *
 * @  ?àò?†ï?ùº            ?àò?†ï?ûê       ?àò?†ï?Ç¥?ö©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ?†ïÎØºÏÑú       ÏµúÏ¥à?Éù?Ñ±
 *
 * @author ?†ïÎØºÏÑú
 * @since  2023.02.08
 *
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final HttpStatus HTTP_STATUS_OK = HttpStatus.OK;

    /**
     * [Exception] API ?ò∏Ï∂? ?ãú 'Í∞ùÏ≤¥' ?òπ?? '?åå?ùºÎØ∏ÌÑ∞' ?ç∞?ù¥?Ñ∞ Í∞íÏù¥ ?ú†?ö®?ïòÏß? ?ïä?? Í≤ΩÏö∞
     *
     * @param ex MethodArgumentNotValidException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("handleMethodArgumentNotValidException", ex);
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField()).append(":");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(", ");
        }
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_VALID_ERROR, String.valueOf(stringBuilder));
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    /**
     * [Exception] API ?ò∏Ï∂? ?ãú 'Header' ?Ç¥?óê ?ç∞?ù¥?Ñ∞ Í∞íÏù¥ ?ú†?ö®?ïòÏß? ?ïä?? Í≤ΩÏö∞
     *
     * @param ex MissingRequestHeaderException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        log.error("MissingRequestHeaderException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    /**
     * [Exception] ?Å¥?ùº?ù¥?ñ∏?ä∏?óê?Ñú BodyÎ°? 'Í∞ùÏ≤¥' ?ç∞?ù¥?Ñ∞Í∞? ?Ñò?ñ¥?ò§Ïß? ?ïä?ïò?ùÑ Í≤ΩÏö∞
     *
     * @param ex HttpMessageNotReadableException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * [Exception] ?Å¥?ùº?ù¥?ñ∏?ä∏?óê?Ñú requestÎ°? '?åå?ùºÎØ∏ÌÑ∞Î°?' ?ç∞?ù¥?Ñ∞Í∞? ?Ñò?ñ¥?ò§Ïß? ?ïä?ïò?ùÑ Í≤ΩÏö∞
     *
     * @param ex MissingServletRequestParameterException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderExceptionException(
            MissingServletRequestParameterException ex) {
        log.error("handleMissingServletRequestParameterException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.MISSING_REQUEST_PARAMETER_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /**
     * [Exception] ?ûòÎ™ªÎêú ?ÑúÎ≤? ?öîÏ≤??ùº Í≤ΩÏö∞ Î∞úÏÉù?ïú Í≤ΩÏö∞
     *
     * @param e HttpClientErrorException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    protected ResponseEntity<ErrorResponse> handleBadRequestException(HttpClientErrorException e) {
        log.error("HttpClientErrorException.BadRequest", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.BAD_REQUEST_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }


    /**
     * [Exception] ?ûòÎ™ªÎêú Ï£ºÏÜåÎ°? ?öîÏ≤? ?ïú Í≤ΩÏö∞
     *
     * @param e NoHandlerFoundException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoHandlerFoundExceptionException(NoHandlerFoundException e) {
        log.error("handleNoHandlerFoundExceptionException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NOT_FOUND_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }


    /**
     * [Exception] NULL Í∞íÏù¥ Î∞úÏÉù?ïú Í≤ΩÏö∞
     *
     * @param e NullPointerException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
        log.error("handleNullPointerException", e);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.NULL_POINT_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    /**
     * Input / Output ?Ç¥?óê?Ñú Î∞úÏÉù?ïú Í≤ΩÏö∞
     *
     * @param ex IOException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
        log.error("handleIOException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.IO_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }


    /**
     * com.google.gson ?Ç¥?óê Exception Î∞úÏÉù?ïò?äî Í≤ΩÏö∞
     *
     * @param ex JsonParseException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(JsonParseException.class)
    protected ResponseEntity<ErrorResponse> handleJsonParseExceptionException(JsonParseException ex) {
        log.error("handleJsonParseExceptionException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.JSON_PARSE_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }

    /**
     * com.fasterxml.jackson.core ?Ç¥?óê Exception Î∞úÏÉù?ïò?äî Í≤ΩÏö∞
     *
     * @param ex JsonProcessingException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(JsonProcessingException.class)
    protected ResponseEntity<ErrorResponse> handleJsonProcessingException(JsonProcessingException ex) {
        log.error("handleJsonProcessingException", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.REQUEST_BODY_MISSING_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }


    // ==================================================================================================================

    /**
     * [Exception] Î™®Îì† Exception Í≤ΩÏö∞ Î∞úÏÉù
     *
     * @param ex Exception
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(Exception.class)
    protected final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("Exception", ex);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(response, HTTP_STATUS_OK);
    }
}
