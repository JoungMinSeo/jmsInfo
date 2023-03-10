package com.levelup.jmsinfo.cmmn.code;

import lombok.Getter;
/**
 * @purpose [κ³΅ν΅ μ½λ] API ?΅? ? ??? '??¬ μ½λ'λ₯? Enum ??λ‘? κ΄?λ¦¬λ?? ??€.
 * Success CodeList : ?±κ³? μ½λλ₯? κ΄?λ¦¬ν?€.
 * Success Code Constructor: ?±κ³? μ½λλ₯? ?¬?©?κΈ? ?? ??±?λ₯? κ΅¬μ±??€.
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
public enum SuccessCode {

    /**
     * ******************************* Success CodeList ***************************************
     */
    // μ‘°ν ?±κ³? μ½λ (HTTP Response: 200 OK)
    SELECT_SUCCESS(200, "200", "SELECT SUCCESS"),
    // ?­?  ?±κ³? μ½λ (HTTP Response: 200 OK)
    DELETE_SUCCESS(200, "200", "DELETE SUCCESS"),
    // ?½? ?±κ³? μ½λ (HTTP Response: 201 Created)
    INSERT_SUCCESS(201, "201", "INSERT SUCCESS"),
    // ??  ?±κ³? μ½λ (HTTP Response: 201 Created)
    UPDATE_SUCCESS(204, "204", "UPDATE SUCCESS"),
    
    // λ‘κ·Έ?? ?±κ³? μ½λ
    LOGOUT_SUCCESS(200, "200", "LOGOUT SUCCESS"),
    
    ; // End

    /**
     * ******************************* Success Code Constructor ***************************************
     */
    // ?±κ³? μ½λ? 'μ½λ ??'λ₯? λ°ν??€.
    private final int status;

    // ?±κ³? μ½λ? 'μ½λ κ°?'? λ°ν??€.
    private final String code;

    // ?±κ³? μ½λ? 'μ½λ λ©μμ§?'λ₯? λ°ν??€.s
    private final String message;

    // ??±? κ΅¬μ±
    SuccessCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
