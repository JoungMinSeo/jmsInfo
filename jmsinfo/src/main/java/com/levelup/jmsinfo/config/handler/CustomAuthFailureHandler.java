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
 * @purpose ?��?��?��?�� ?�인증�?�에 ???�� ?��?��?��???�� 경우 ?��?��?��?�� Handler�? ?��?��?�� ???�� ?��?��?��?���? 반환값을 구성?��?�� ?��?��
 * 
 * @  ?��?��?��            ?��?��?��       ?��?��?��?��
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ?��민서       최초?��?��
 *
 * @author ?��민서
 * @since  2023.02.08
 *
 */
@Slf4j
@Configuration
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        // [STEP1] ?��?��?��?��?���? ?��?�� ?�� ?��?�� 값을 구성?��?��?��.
        JSONObject jsonObject = new JSONObject();
        String failMsg = "";

        // [STEP2] 발생?�� Exception ?�� ???��?�� ?��?��?��?��?��.
        if (exception instanceof AuthenticationServiceException) {
            failMsg = "로그?�� ?��보�? ?��치하�? ?��?��?��?��.";

        } else if (exception instanceof BadCredentialsException) {
            failMsg = "비�?번호�? ?��치하�? ?��?��?��?��.";

        } else if (exception instanceof LockedException) {
            failMsg = "로그?�� ?��보�? ?��치하�? ?��?��?��?��.";

        } else if (exception instanceof DisabledException) {
            failMsg = "로그?�� ?��보�? ?��치하�? ?��?��?��?��.";

        } else if (exception instanceof AccountExpiredException) {
            failMsg = "로그?�� ?��보�? ?��치하�? ?��?��?��?��.";

        } else if (exception instanceof CredentialsExpiredException) {
            failMsg = "로그?�� ?��보�? ?��치하�? ?��?��?��?��.";
        }
        // [STEP3] ?��?�� 값을 구성?���? ?��?��?��?��?��.
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