package com.levelup.jmsinfo.config.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @purpose ?¬?©?? ?μΈμ¦β?μ ???΄ ?€?¨???? κ²½μ° ???? Handlerλ‘? ?€?¨? ??? ?¬?©??κ²? λ°νκ°μ κ΅¬μ±??¬ ? ?¬
 * 
 * @  ?? ?Ό            ?? ?       ?? ?΄?©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ? λ―Όμ       μ΅μ΄??±
 *
 * @author ? λ―Όμ
 * @since  2023.02.08
 *
 */
@Slf4j
@Configuration
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        // [STEP1] ?΄?Ό?΄?Έ?Έλ‘? ? ?¬ ?  ??΅ κ°μ κ΅¬μ±?©??€.
        JSONObject jsonObject = new JSONObject();
        String failMsg = "";

        // [STEP2] λ°μ? Exception ? ???΄? ??Έ?©??€.
        if (exception instanceof AuthenticationServiceException) {
            failMsg = "λ‘κ·Έ?Έ ? λ³΄κ? ?ΌμΉνμ§? ??΅??€.";

        } else if (exception instanceof BadCredentialsException) {
            failMsg = "λΉλ?λ²νΈκ°? ?ΌμΉνμ§? ??΅??€.";

        } else if (exception instanceof LockedException) {
            failMsg = "λ‘κ·Έ?Έ ? λ³΄κ? ?ΌμΉνμ§? ??΅??€.";

        } else if (exception instanceof DisabledException) {
            failMsg = "λ‘κ·Έ?Έ ? λ³΄κ? ?ΌμΉνμ§? ??΅??€.";

        } else if (exception instanceof AccountExpiredException) {
            failMsg = "λ‘κ·Έ?Έ ? λ³΄κ? ?ΌμΉνμ§? ??΅??€.";

        } else if (exception instanceof CredentialsExpiredException) {
            failMsg = "λ‘κ·Έ?Έ ? λ³΄κ? ?ΌμΉνμ§? ??΅??€.";
        }
        // [STEP3] ??΅ κ°μ κ΅¬μ±?κ³? ? ?¬?©??€.
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();

        log.debug(failMsg);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("userInfo", null);
        resultMap.put("resultCode", 9999);
        resultMap.put("failMsg", failMsg);
        jsonObject = new JSONObject(resultMap);

        printWriter.print(jsonObject);
        printWriter.flush();
        printWriter.close();
    }
}