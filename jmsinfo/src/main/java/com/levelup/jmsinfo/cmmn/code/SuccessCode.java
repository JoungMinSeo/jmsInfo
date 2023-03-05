package com.levelup.jmsinfo.cmmn.code;

import lombok.Getter;
/**
 * @purpose [공통 코드] API ?��?��?�� ???�� '?��?�� 코드'�? Enum ?��?���? �?리�?? ?��?��.
 * Success CodeList : ?���? 코드�? �?리한?��.
 * Success Code Constructor: ?���? 코드�? ?��?��?���? ?��?�� ?��?��?���? 구성?��?��.
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
public enum SuccessCode {

    /**
     * ******************************* Success CodeList ***************************************
     */
    // 조회 ?���? 코드 (HTTP Response: 200 OK)
    SELECT_SUCCESS(200, "200", "SELECT SUCCESS"),
    // ?��?�� ?���? 코드 (HTTP Response: 200 OK)
    DELETE_SUCCESS(200, "200", "DELETE SUCCESS"),
    // ?��?�� ?���? 코드 (HTTP Response: 201 Created)
    INSERT_SUCCESS(201, "201", "INSERT SUCCESS"),
    // ?��?�� ?���? 코드 (HTTP Response: 201 Created)
    UPDATE_SUCCESS(204, "204", "UPDATE SUCCESS"),
    
    // 로그?��?�� ?���? 코드
    LOGOUT_SUCCESS(200, "200", "LOGOUT SUCCESS"),
    
    ; // End

    /**
     * ******************************* Success Code Constructor ***************************************
     */
    // ?���? 코드?�� '코드 ?��?��'�? 반환?��?��.
    private final int status;

    // ?���? 코드?�� '코드 �?'?�� 반환?��?��.
    private final String code;

    // ?���? 코드?�� '코드 메시�?'�? 반환?��?��.s
    private final String message;

    // ?��?��?�� 구성
    SuccessCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
