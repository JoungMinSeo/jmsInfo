package com.levelup.jmsinfo.cmmn.code;

import lombok.Getter;
/**
 * @purpose [ê³µí†µ ì½”ë“œ] API ?†µ?‹ ?— ???•œ '?—?Ÿ¬ ì½”ë“œ'ë¥? Enum ?˜•?ƒœë¡? ê´?ë¦¬ë?? ?•œ?‹¤.
 * Success CodeList : ?„±ê³? ì½”ë“œë¥? ê´?ë¦¬í•œ?‹¤.
 * Success Code Constructor: ?„±ê³? ì½”ë“œë¥? ?‚¬?š©?•˜ê¸? ?œ„?•œ ?ƒ?„±?ë¥? êµ¬ì„±?•œ?‹¤.
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
public enum SuccessCode {

    /**
     * ******************************* Success CodeList ***************************************
     */
    // ì¡°íšŒ ?„±ê³? ì½”ë“œ (HTTP Response: 200 OK)
    SELECT_SUCCESS(200, "200", "SELECT SUCCESS"),
    // ?‚­? œ ?„±ê³? ì½”ë“œ (HTTP Response: 200 OK)
    DELETE_SUCCESS(200, "200", "DELETE SUCCESS"),
    // ?‚½?… ?„±ê³? ì½”ë“œ (HTTP Response: 201 Created)
    INSERT_SUCCESS(201, "201", "INSERT SUCCESS"),
    // ?ˆ˜? • ?„±ê³? ì½”ë“œ (HTTP Response: 201 Created)
    UPDATE_SUCCESS(204, "204", "UPDATE SUCCESS"),
    
    // ë¡œê·¸?•„?›ƒ ?„±ê³? ì½”ë“œ
    LOGOUT_SUCCESS(200, "200", "LOGOUT SUCCESS"),
    
    ; // End

    /**
     * ******************************* Success Code Constructor ***************************************
     */
    // ?„±ê³? ì½”ë“œ?˜ 'ì½”ë“œ ?ƒ?ƒœ'ë¥? ë°˜í™˜?•œ?‹¤.
    private final int status;

    // ?„±ê³? ì½”ë“œ?˜ 'ì½”ë“œ ê°?'?„ ë°˜í™˜?•œ?‹¤.
    private final String code;

    // ?„±ê³? ì½”ë“œ?˜ 'ì½”ë“œ ë©”ì‹œì§?'ë¥? ë°˜í™˜?•œ?‹¤.s
    private final String message;

    // ?ƒ?„±? êµ¬ì„±
    SuccessCode(final int status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
