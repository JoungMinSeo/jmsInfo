package com.levelup.jmsinfo.cmmn.response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.validation.BindingResult;

import com.levelup.jmsinfo.cmmn.code.ErrorCode;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @purpose Global Exception Handler?—?„œ ë°œìƒ?•œ ?—?Ÿ¬?— ???•œ ?‘?‹µ ì²˜ë¦¬ë¥? ê´?ë¦?
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse {
    private int status;                 // ?—?Ÿ¬ ?ƒ?ƒœ ì½”ë“œ
    private String divisionCode;        // ?—?Ÿ¬ êµ¬ë¶„ ì½”ë“œ
    private String resultMsg;           // ?—?Ÿ¬ ë©”ì‹œì§?
    private List<FieldError> errors;    // ?ƒ?„¸ ?—?Ÿ¬ ë©”ì‹œì§?
    private String reason;              // ?—?Ÿ¬ ?´?œ 

    /**
     * ErrorResponse ?ƒ?„±?-1
     *
     * @param code ErrorCode
     */
    @Builder
    protected ErrorResponse(final ErrorCode code) {
        this.resultMsg = code.getMessage();
        this.status = code.getStatus();
        this.divisionCode = code.getDivisionCode();
        this.errors = new ArrayList<>();
    }

    /**
     * ErrorResponse ?ƒ?„±?-2
     *
     * @param code   ErrorCode
     * @param reason String
     */
    @Builder
    protected ErrorResponse(final ErrorCode code, final String reason) {
        this.resultMsg = code.getMessage();
        this.status = code.getStatus();
        this.divisionCode = code.getDivisionCode();
        this.reason = reason;
    }

    /**
     * ErrorResponse ?ƒ?„±?-3
     *
     * @param code   ErrorCode
     * @param errors List<FieldError>
     */
    @Builder
    protected ErrorResponse(final ErrorCode code, final List<FieldError> errors) {
        this.resultMsg = code.getMessage();
        this.status = code.getStatus();
        this.errors = errors;
        this.divisionCode = code.getDivisionCode();
    }


    /**
     * Global Exception ? „?†¡ ???…-1
     *
     * @param code          ErrorCode
     * @param bindingResult BindingResult
     * @return ErrorResponse
     */
    public static ErrorResponse of(final ErrorCode code, final BindingResult bindingResult) {
        return new ErrorResponse(code, FieldError.of(bindingResult));
    }

    /**
     * Global Exception ? „?†¡ ???…-2
     *
     * @param code ErrorCode
     * @return ErrorResponse
     */
    public static ErrorResponse of(final ErrorCode code) {
        return new ErrorResponse(code);
    }

    /**
     * Global Exception ? „?†¡ ???…-3
     *
     * @param code   ErrorCode
     * @param reason String
     * @return ErrorResponse
     */
    public static ErrorResponse of(final ErrorCode code, final String reason) {
        return new ErrorResponse(code, reason);
    }


    /**
     * ?—?Ÿ¬ë¥? e.getBindingResult() ?˜•?ƒœë¡? ? „?‹¬ ë°›ëŠ” ê²½ìš° ?•´?‹¹ ?‚´?š©?„ ?ƒ?„¸ ?‚´?š©?œ¼ë¡? ë³?ê²½í•˜?Š” ê¸°ëŠ¥?„ ?ˆ˜?–‰?•œ?‹¤.
     */
    @Getter
    public static class FieldError {
        private final String field;
        private final String value;
        private final String reason;

        public static List<FieldError> of(final String field, final String value, final String reason) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, value, reason));
            return fieldErrors;
        }

        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }

        @Builder
        FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }
    }
}
