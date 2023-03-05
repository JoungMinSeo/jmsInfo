package com.levelup.jmsinfo.cmmn.exception;

import com.levelup.jmsinfo.cmmn.code.ErrorCode;

import lombok.Builder;
import lombok.Getter;

/**
 * @purpose ?��?���? ?��?��?���? ?��?�� 구현�?
 *
 * @  ?��?��?��            ?��?��?��       ?��?��?��?��
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ?��민서       최초?��?��
 *
 * @author ?��민서
 * @since  2023.02.08
 *
 */
public class BusinessExceptionHandler extends RuntimeException {
	
	@Getter
    private final ErrorCode errorCode;

    @Builder
    public BusinessExceptionHandler(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    @Builder
    public BusinessExceptionHandler(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
