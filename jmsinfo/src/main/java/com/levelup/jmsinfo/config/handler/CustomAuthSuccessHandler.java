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
 * @purpose ?¬?©?? ?μΈμ¦β?μ ???΄ ?±κ³΅ν??? κ²½μ° ???? Handlerλ‘? ?±κ³΅μ ??? ?¬?©??κ²? λ°νκ°μ κ΅¬μ±??¬ ? ?¬
 * 
 * @  ?? ?Ό            ?? ?       ?? ?΄?©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ? λ―Όμ       μ΅μ΄??±
 * @ 2023.02.22       ? λ―Όμ       Redis? Refresh Token ???₯ λ‘μ§ μΆκ?
 *
 * @author ? λ―Όμ
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

        // [STEP 1] ?¬?©??? κ΄?? ¨? ? λ³΄λ?? λͺ¨λ μ‘°ν?©??€.
        UserVO userVo = (UserVO) authentication.getPrincipal();

        // [STEP 2] μ‘°ν? ?°?΄?°λ₯? JSONObject ??λ‘? ??±? ???©??€.
        ObjectMapper mapper = new ObjectMapper(); 
        String userVoObj = mapper.writeValueAsString(userVo);
        
        HashMap<String, Object> responseMap = new HashMap<>();

        JSONObject jsonObject = new JSONObject();
        
        // [STEP 3] ? ?¬?  ?°?΄?° κ°? κ΅¬μ±
        responseMap.put("userInfo", userVoObj);
        responseMap.put("resultCode", 200);
        responseMap.put("failMsg", null);
        jsonObject = new JSONObject(responseMap);

        // [STEP 4] JWT λ°κΈ
        String accessToken = TokenUtils.generateAccessToken(userVo);
        String refreshToken = TokenUtils.generateRefreshToken(userVo);
        
        // [STEP 5] header? Access ? ?° λ°? Refresh ? ?° κ°? μΆκ?
        response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + accessToken);
        response.addHeader(AuthConstants.AUTH_RF_TOKEN, AuthConstants.TOKEN_TYPE + " " + refreshToken);
        
        // [STEP 6] Redis? RefreshToken ???₯ 
        // TODO: Redis Time ? ?΄?Ό?¨
        redisService.setValues(userVo.getUserId(), refreshToken, Duration.ofMinutes(10));
        
        // [STEP 7] κ΅¬μ±? ??΅ κ°μ ? ?¬?©??€.
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jsonObject);  // μ΅μ’ ???₯? '?¬?©? ? λ³?', '?¬?΄?Έ ? λ³?' Front ? ?¬
        printWriter.flush();
        printWriter.close();
    }
}
