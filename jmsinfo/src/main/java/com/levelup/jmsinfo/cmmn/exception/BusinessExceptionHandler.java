package com.levelup.jmsinfo.cmmn.exception;

import com.levelup.jmsinfo.cmmn.code.ErrorCode;

import lombok.Builder;
import lombok.Getter;

/**
 * @purpose ?—?Ÿ¬ë¥? ?‚¬?š©?•˜ê¸? ?œ„?•œ êµ¬í˜„ì²?
 *
 * @  ?ˆ˜? •?¼            ?ˆ˜? •?       ?ˆ˜? •?‚´?š©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ? •ë¯¼ì„œ       ìµœì´ˆ?ƒ?„±
 *
 * @author ? •ë¯¼ì„œ
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
