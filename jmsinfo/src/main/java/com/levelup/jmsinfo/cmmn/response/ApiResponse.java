package com.levelup.jmsinfo.cmmn.response;

import lombok.Builder;
import lombok.Getter;

/**
 * @purpose [ê³µí†µ] API Response ê²°ê³¼?˜ ë°˜í™˜ ê°’ì„ ê´?ë¦?
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
public class ApiResponse<T> {

    // API ?‘?‹µ ê²°ê³¼ Response
    private T result;

    // API ?‘?‹µ ì½”ë“œ Response
    private int resultCode;

    // API ?‘?‹µ ì½”ë“œ Message
    private String resultMsg;

    @Builder
    public ApiResponse(final T result, final int resultCode, final String resultMsg) {
        this.result = result;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

}
