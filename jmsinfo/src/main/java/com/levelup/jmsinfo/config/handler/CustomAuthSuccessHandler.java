package com.levelup.jmsinfo.config.handler;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.levelup.jmsinfo.config.AuthConstants;
import com.levelup.jmsinfo.config.TokenUtils;
import com.levelup.jmsinfo.config.service.RedisService;
import com.levelup.jmsinfo.user.vo.UserVO;

import lombok.extern.slf4j.Slf4j;

/**
 * @purpose ?‚¬?š©??˜ ?˜ì¸ì¦â?™ì— ???•´ ?„±ê³µí•˜???„ ê²½ìš° ?ˆ˜?–‰?˜?Š” Handlerë¡? ?„±ê³µì— ???•œ ?‚¬?š©??—ê²? ë°˜í™˜ê°’ì„ êµ¬ì„±?•˜?—¬ ? „?‹¬
 * 
 * @  ?ˆ˜? •?¼            ?ˆ˜? •?       ?ˆ˜? •?‚´?š©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ? •ë¯¼ì„œ       ìµœì´ˆ?ƒ?„±
 * @ 2023.02.22       ? •ë¯¼ì„œ       Redis?— Refresh Token ???¥ ë¡œì§ ì¶”ê?
 *
 * @author ? •ë¯¼ì„œ
 * @since  2023.02.08
 *
 */
@Slf4j
@Configuration
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Autowired
	RedisService redisService;
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
    	log.info("3.CustomLoginSuccessHandler");

        // [STEP 1] ?‚¬?š©??? ê´?? ¨?œ ? •ë³´ë?? ëª¨ë‘ ì¡°íšŒ?•©?‹ˆ?‹¤.
        UserVO userVo = (UserVO) authentication.getPrincipal();

        // [STEP 2] ì¡°íšŒ?•œ ?°?´?„°ë¥? JSONObject ?˜•?ƒœë¡? ?ŒŒ?‹±?„ ?ˆ˜?–‰?•©?‹ˆ?‹¤.
        ObjectMapper mapper = new ObjectMapper(); 
        String userVoObj = mapper.writeValueAsString(userVo);
        
        HashMap<String, Object> responseMap = new HashMap<>();

        JSONObject jsonObject = new JSONObject();
        
        // [STEP 3] ? „?‹¬?•  ?°?´?„° ê°? êµ¬ì„±
        responseMap.put("userInfo", userVoObj);
        responseMap.put("resultCode", 200);
        responseMap.put("failMsg", null);
        jsonObject = new JSONObject(responseMap);

        // [STEP 4] JWT ë°œê¸‰
        String accessToken = TokenUtils.generateAccessToken(userVo);
        String refreshToken = TokenUtils.generateRefreshToken(userVo);
        
        // [STEP 5] header?— Access ?† ?° ë°? Refresh ?† ?° ê°? ì¶”ê?
        response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + accessToken);
        response.addHeader(AuthConstants.AUTH_RF_TOKEN, AuthConstants.TOKEN_TYPE + " " + refreshToken);
        
        // [STEP 6] Redis?— RefreshToken ???¥ 
        // TODO: Redis Time ? •?•´?•¼?•¨
        redisService.setValues(userVo.getUserId(), refreshToken, Duration.ofMinutes(10));
        
        // [STEP 7] êµ¬ì„±?•œ ?‘?‹µ ê°’ì„ ? „?‹¬?•©?‹ˆ?‹¤.
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jsonObject);  // ìµœì¢… ???¥?œ '?‚¬?š©? ? •ë³?', '?‚¬?´?Š¸ ? •ë³?' Front ? „?‹¬
        printWriter.flush();
        printWriter.close();
    }
}
