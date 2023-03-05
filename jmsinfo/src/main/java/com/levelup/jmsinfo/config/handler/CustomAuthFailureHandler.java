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
 * @purpose ?‚¬?š©??˜ ?˜ì¸ì¦â?™ì— ???•´ ?‹¤?Œ¨?•˜???„ ê²½ìš° ?ˆ˜?–‰?˜?Š” Handlerë¡? ?‹¤?Œ¨?— ???•œ ?‚¬?š©??—ê²? ë°˜í™˜ê°’ì„ êµ¬ì„±?•˜?—¬ ? „?‹¬
 * 
 * @  ?ˆ˜? •?¼            ?ˆ˜? •?       ?ˆ˜? •?‚´?š©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ? •ë¯¼ì„œ       ìµœì´ˆ?ƒ?„±
 *
 * @author ? •ë¯¼ì„œ
 * @since  2023.02.08
 *
 */
@Slf4j
@Configuration
public class CustomAuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        // [STEP1] ?´?¼?´?–¸?Š¸ë¡? ? „?‹¬ ?•  ?‘?‹µ ê°’ì„ êµ¬ì„±?•©?‹ˆ?‹¤.
        JSONObject jsonObject = new JSONObject();
        String failMsg = "";

        // [STEP2] ë°œìƒ?•œ Exception ?— ???•´?„œ ?™•?¸?•©?‹ˆ?‹¤.
        if (exception instanceof AuthenticationServiceException) {
            failMsg = "ë¡œê·¸?¸ ? •ë³´ê? ?¼ì¹˜í•˜ì§? ?•Š?Šµ?‹ˆ?‹¤.";

        } else if (exception instanceof BadCredentialsException) {
            failMsg = "ë¹„ë?ë²ˆí˜¸ê°? ?¼ì¹˜í•˜ì§? ?•Š?Šµ?‹ˆ?‹¤.";

        } else if (exception instanceof LockedException) {
            failMsg = "ë¡œê·¸?¸ ? •ë³´ê? ?¼ì¹˜í•˜ì§? ?•Š?Šµ?‹ˆ?‹¤.";

        } else if (exception instanceof DisabledException) {
            failMsg = "ë¡œê·¸?¸ ? •ë³´ê? ?¼ì¹˜í•˜ì§? ?•Š?Šµ?‹ˆ?‹¤.";

        } else if (exception instanceof AccountExpiredException) {
            failMsg = "ë¡œê·¸?¸ ? •ë³´ê? ?¼ì¹˜í•˜ì§? ?•Š?Šµ?‹ˆ?‹¤.";

        } else if (exception instanceof CredentialsExpiredException) {
            failMsg = "ë¡œê·¸?¸ ? •ë³´ê? ?¼ì¹˜í•˜ì§? ?•Š?Šµ?‹ˆ?‹¤.";
        }
        // [STEP3] ?‘?‹µ ê°’ì„ êµ¬ì„±?•˜ê³? ? „?‹¬?•©?‹ˆ?‹¤.
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