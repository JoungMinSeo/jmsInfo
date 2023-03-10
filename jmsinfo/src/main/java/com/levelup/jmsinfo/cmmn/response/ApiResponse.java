package com.levelup.jmsinfo.cmmn.response;

import lombok.Builder;
import lombok.Getter;

/**
 * @purpose [κ³΅ν΅] API Response κ²°κ³Ό? λ°ν κ°μ κ΄?λ¦?
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
public class ApiResponse<T> {

    // API ??΅ κ²°κ³Ό Response
    private T result;

    // API ??΅ μ½λ Response
    private int resultCode;

    // API ??΅ μ½λ Message
    private String resultMsg;

    @Builder
    public ApiResponse(final T result, final int resultCode, final String resultMsg) {
        this.result = result;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

}
