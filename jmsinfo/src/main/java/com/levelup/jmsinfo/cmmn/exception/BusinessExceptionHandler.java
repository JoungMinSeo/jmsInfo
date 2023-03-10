package com.levelup.jmsinfo.cmmn.exception;

import com.levelup.jmsinfo.cmmn.code.ErrorCode;

import lombok.Builder;
import lombok.Getter;

/**
 * @purpose ??¬λ₯? ?¬?©?κΈ? ?? κ΅¬νμ²?
 *
 * @  ?? ?Ό            ?? ?       ?? ?΄?©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ? λ―Όμ       μ΅μ΄??±
 *
 * @author ? λ―Όμ
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
