package com.levelup.jmsinfo.config.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.levelup.jmsinfo.cmmn.code.ErrorCode;
import com.levelup.jmsinfo.cmmn.exception.BusinessExceptionHandler;
import com.levelup.jmsinfo.config.AuthConstants;
import com.levelup.jmsinfo.config.TokenUtils;
import com.levelup.jmsinfo.config.service.CommonService;
import com.levelup.jmsinfo.config.service.RedisService;
import com.levelup.jmsinfo.user.vo.UserVO;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SignatureException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @purpose μ§?? ? URL λ³? JWT ? ?¨?± κ²?μ¦μ ???λ©? μ§μ ? ?Έ ?¬?©? '?Έμ¦?'? ??Έ
 * 
 * @  ?? ?Ό            ?? ?      ?? ?΄?©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.09       ? λ―Όμ      μ΅μ΄??±
 * @ 2023.02.24       ? λ―Όμ      filter μ‘°κ±΄ λ³?κ²?
 * @ 2023.02.28       ? λ―Όμ      Access ? ?° ?¬λ°κΈ μΆκ?
 *
 * @author ? λ―Όμ
 * @since  2023.02.09
 *
 */
@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	@Autowired
	RedisService redisService;
	
	@Autowired
	CommonService commonService;
	
//	@Resource
//    private UserDetailsService userDetailsService;
	
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws IOException, ServletException {

    	log.info("=========== request URL ===========> " + request.getRequestURI());
    	
        // 1. ? ?°?΄ ???μ§? ??? API URL? ???΄? λ°°μ΄λ‘? κ΅¬μ±?©??€.
        List<String> list = Arrays.asList(
        		"/",
                "/user/login",
                "/favicon.ico",
                "/user/insertUser",
                "/user/logout",
                "/noAuth/agreementPage"
        );

        // 2. ? ?°?΄ ???μ§? ??? API URL? κ²½μ° => λ‘μ§ μ²λ¦¬ ??΄ ?€? ??°λ‘? ?΄?
        if (list.contains(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        
        // 3. ? ?°?΄ ???μ§? ??? resource ?μ²?? κ²½μ° => λ‘μ§ μ²λ¦¬ ??΄ ?€? ??°λ‘? ?΄?
        if (request.getRequestURI().contains("/resource/")) {
            chain.doFilter(request, response);
            return;
        }
        
        // 4. ? ?°?΄ ???μ§? ??? resource ?μ²?? κ²½μ° => λ‘μ§ μ²λ¦¬ ??΄ ?€? ??°λ‘? ?΄?
        if (request.getRequestURI().contains("/noAuth/")) {
            chain.doFilter(request, response);
            return;
        }

        // 5. OPTIONS ?μ²??Ό κ²½μ° => λ‘μ§ μ²λ¦¬ ??΄ ?€? ??°λ‘? ?΄?
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }
        
        try {
        	
	        // [STEP 1] Client?? APIλ₯? ?μ²??  ? Header? AccessTokenκ³? RefreshToken? ??Έ?©??€.
	        String headerAT = request.getHeader(AuthConstants.AUTH_HEADER);
	        String headerRT = request.getHeader(AuthConstants.AUTH_RF_TOKEN);
	        
	        // [STEP 2] Header ?΄? Access ? κ·Όμ μΆμΆ?©??€.
	        String tokenAT = TokenUtils.getTokenFromHeader(headerAT);
	        
	        // [STEP 3] Redis?? Access ? ?° κ°μΌλ‘? BlackList μ‘°ν
	        String blackList = redisService.getValues(tokenAT);
	        
	        if(blackList != null && !blackList.equals("")) {  
	        	throw new BusinessExceptionHandler(ErrorCode.RELOGIN_TOKEN_ERROR.getMessage(), ErrorCode.RELOGIN_TOKEN_ERROR);
	        }
	        
	        // [STEP 4] Header? Access Tokenλ§? ?? κ²½μ°, Access Token? ??? ?Έμ¦λ§ ???κ³? API Response μ§ν?©??€.
	        if(headerRT == null) {
	        	
	        	// [STEP 5] μΆμΆ? Access ? ?°?΄ ? ?¨?μ§? ?¬λΆ?λ₯? μ²΄ν¬?©??€.
	        	if(TokenUtils.isValidToken(tokenAT)) {
	        		
	        		// [STEP 5-1] ? ?°? κΈ°λ°?Όλ‘? ?¬?©? ??΄?λ₯? λ°ν λ°μ΅??€.
	        		String userId = TokenUtils.getUserIdFromToken(tokenAT, "userId");
	        		log.debug("userId Check : " + userId);
	        		
	        		// [STEP 5-2] ?¬?©? ??΄?κ°? μ‘΄μ¬??μ§? ?¬λΆ?λ₯? μ²΄ν¬?©??€.
					if (userId != null && !userId.equalsIgnoreCase("")) {
						chain.doFilter(request, response);
					} else { // ?¬?©? ??΄?κ°? μ‘΄μ¬?μ§? ?? κ²½μ°
						throw new BusinessExceptionHandler(ErrorCode.RELOGIN_TOKEN_ERROR.getMessage(), ErrorCode.RELOGIN_TOKEN_ERROR);
					}
					
	        	}else { // Access Token?΄ ? ?¨?μ§? ?? κ²½μ°
	        		throw new BusinessExceptionHandler(ErrorCode.RELOGIN_TOKEN_ERROR.getMessage(), ErrorCode.RELOGIN_TOKEN_ERROR);
	        	}
	        	
	        	
	        }else { // [STEP 6] Header? Refresh Token?΄ ?? κ²½μ°, Refresh Token? ??? ?Έμ¦? ??
	        	
	        	// [STEP 6-1] Header ?΄? ? κ·Όμ μΆμΆ?©??€.
	        	String tokenRT = TokenUtils.getTokenFromHeader(headerRT);
	            
	        	// [STEP 7] μΆμΆ? Refresh ? κ·Όμ΄ ? ?¨?μ§? ?¬λΆ?λ₯? μ²΄ν¬?©??€.
	        	if(TokenUtils.isValidToken(tokenRT)) {
	
	        		// [STEP 7-1] Access Token?? userId μΆμΆ?©??€.
	        		String userId = commonService.getJsonData(TokenUtils.getTokenPayload(tokenAT), "userId");
	        		
	        		// [STEP 7-2] Redis?? userIdλ‘? Refresh ? ?° μΆμΆ?©??€.
	        		String tokenRTFromRedis = redisService.getValues(userId);
	        		
	        		// [STEP 8] Header ?΄? Refresh ? ?°κ³? Redis?? μ‘°ν?΄?¨ Refresh ? ?° λΉκ΅??¬ κ°μ κ²½μ° Access ? ?°? ?¬λ°κΈ ?©??€.
	        		if(tokenRT.equals(tokenRTFromRedis)) {
	        			
	        			// [STEP 8-1] Access ? ?°? ?¬λ°κΈ??¬ response Header? ?΄κ³? API Response μ§ν
	        			String userNo = commonService.getJsonData(TokenUtils.getTokenPayload(tokenAT), "userNo");
	        			String userName = commonService.getJsonData(TokenUtils.getTokenPayload(tokenAT), "userName");
	        			
	        			UserVO user = UserVO.builder()
	        							.userNo(userNo)
	        							.userId(userId)
	        							.userName(userName)
	        							.build();
	        			
	        			String accessToken = TokenUtils.generateAccessToken(user);
	        			response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + accessToken);
	        			
	        			chain.doFilter(request, response);
	        			
	        		}else { // Refresh ? ?° λΉκ΅ ? κ°μ΄ κ°μ? ?? κ²½μ° ?¬ λ‘κ·Έ?Έ ?μ²? 
	        			throw new BusinessExceptionHandler(ErrorCode.RELOGIN_TOKEN_ERROR.getMessage(), ErrorCode.RELOGIN_TOKEN_ERROR);
	        		}
	        		
	        	}else { // Refresh ? ?°?΄ ? ?¨?μ§? ?? κ²½μ°, ?¬ λ‘κ·Έ?Έ ?μ²?
	        		throw new BusinessExceptionHandler(ErrorCode.RELOGIN_TOKEN_ERROR.getMessage(), ErrorCode.RELOGIN_TOKEN_ERROR);
	        	}
	        	
	        }

            
        } catch (Exception e) {
            // Token ?΄? Exception?΄ λ°μ ???? κ²½μ° => ?΄?Ό?΄?Έ?Έ? ??΅κ°μ λ°ν?κ³? μ’λ£?©??€.
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            JSONObject jsonObject = jsonResponseWrapper(e);
            printWriter.print(jsonObject);
            printWriter.flush();
            printWriter.close();
        }
    }

    /**
     * ? ?° κ΄?? ¨ Exception λ°μ ? ??Έ ??΅κ°? κ΅¬μ±
     *
     * @param e Exception
     * @return JSONObject
     */
    private JSONObject jsonResponseWrapper(Exception e) {

        String resultMsg = "";
        // JWT ? ?° λ§λ£
        if (e instanceof ExpiredJwtException) {
            resultMsg = "TOKEN Expired";
        }
        // JWT ??©? ? ?°?΄ ??
        else if (e instanceof SignatureException) {
            resultMsg = "TOKEN SignatureException Login";
        }
        // JWT ? ?°?΄?? ?€λ₯? λ°μ ?
        else if (e instanceof JwtException) {
            resultMsg = "TOKEN Parsing JwtException";
        }
        // ?€? ? BusinessException ?€λ₯μΌ κ²½μ°
        else if (e instanceof BusinessExceptionHandler) 
        {
            resultMsg = e.getMessage();
         // ?΄?Έ JTW ? ?°?΄?? ?€λ₯? λ°μ
        }else {
        	resultMsg = "OTHER TOKEN ERROR";
        }

        HashMap<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("status", 401);
        jsonMap.put("code", "9999");
        jsonMap.put("message", resultMsg);
        jsonMap.put("reason", e.getMessage());
        JSONObject jsonObject = new JSONObject(jsonMap);
        logger.error(resultMsg, e);
        return jsonObject;
    }
}