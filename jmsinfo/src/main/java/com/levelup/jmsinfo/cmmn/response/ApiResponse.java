package com.levelup.jmsinfo.cmmn.response;

import lombok.Builder;
import lombok.Getter;

/**
 * @purpose [공통] API Response 결과?�� 반환 값을 �?�?
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
public class ApiResponse<T> {

    // API ?��?�� 결과 Response
    private T result;

    // API ?��?�� 코드 Response
    private int resultCode;

    // API ?��?�� 코드 Message
    private String resultMsg;

    @Builder
    public ApiResponse(final T result, final int resultCode, final String resultMsg) {
        this.result = result;
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

}
