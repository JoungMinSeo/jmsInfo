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
 * @purpose ?��?��?��?�� ?�인증�?�에 ???�� ?��공하???�� 경우 ?��?��?��?�� Handler�? ?��공에 ???�� ?��?��?��?���? 반환값을 구성?��?�� ?��?��
 * 
 * @  ?��?��?��            ?��?��?��       ?��?��?��?��
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.08       ?��민서       최초?��?��
 * @ 2023.02.22       ?��민서       Redis?�� Refresh Token ???�� 로직 추�?
 *
 * @author ?��민서
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

        // [STEP 1] ?��?��?��?? �??��?�� ?��보�?? 모두 조회?��?��?��.
        UserVO userVo = (UserVO) authentication.getPrincipal();

        // [STEP 2] 조회?�� ?��?��?���? JSONObject ?��?���? ?��?��?�� ?��?��?��?��?��.
        ObjectMapper mapper = new ObjectMapper(); 
        String userVoObj = mapper.writeValueAsString(userVo);
        
        HashMap<String, Object> responseMap = new HashMap<>();

        JSONObject jsonObject = new JSONObject();
        
        // [STEP 3] ?��?��?�� ?��?��?�� �? 구성
        responseMap.put("userInfo", userVoObj);
        responseMap.put("resultCode", 200);
        responseMap.put("failMsg", null);
        jsonObject = new JSONObject(responseMap);

        // [STEP 4] JWT 발급
        String accessToken = TokenUtils.generateAccessToken(userVo);
        String refreshToken = TokenUtils.generateRefreshToken(userVo);
        
        // [STEP 5] header?�� Access ?��?�� �? Refresh ?��?�� �? 추�?
        response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + accessToken);
        response.addHeader(AuthConstants.AUTH_RF_TOKEN, AuthConstants.TOKEN_TYPE + " " + refreshToken);
        
        // [STEP 6] Redis?�� RefreshToken ???�� 
        // TODO: Redis Time ?��?��?��?��
        redisService.setValues(userVo.getUserId(), refreshToken, Duration.ofMinutes(10));
        
        // [STEP 7] 구성?�� ?��?�� 값을 ?��?��?��?��?��.
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        printWriter.print(jsonObject);  // 최종 ???��?�� '?��?��?�� ?���?', '?��?��?�� ?���?' Front ?��?��
        printWriter.flush();
        printWriter.close();
    }
}
