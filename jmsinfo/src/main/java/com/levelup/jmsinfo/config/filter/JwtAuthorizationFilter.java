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
 * @purpose �??��?�� URL �? JWT ?��?��?�� �?증을 ?��?��?���? 직접?��?�� ?��?��?�� '?���?'?�� ?��?��
 * 
 * @  ?��?��?��            ?��?��?��      ?��?��?��?��
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.09       ?��민서      최초?��?��
 * @ 2023.02.24       ?��민서      filter 조건 �?�?
 * @ 2023.02.28       ?��민서      Access ?��?�� ?��발급 추�?
 *
 * @author ?��민서
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
    	
        // 1. ?��?��?�� ?��?��?���? ?��?? API URL?�� ???��?�� 배열�? 구성?��?��?��.
        List<String> list = Arrays.asList(
        		"/",
                "/user/login",
                "/favicon.ico",
                "/user/insertUser",
                "/user/logout",
                "/noAuth/agreementPage"
        );

        // 2. ?��?��?�� ?��?��?���? ?��?? API URL?�� 경우 => 로직 처리 ?��?�� ?��?�� ?��?���? ?��?��
        if (list.contains(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        
        // 3. ?��?��?�� ?��?��?���? ?��?? resource ?���??�� 경우 => 로직 처리 ?��?�� ?��?�� ?��?���? ?��?��
        if (request.getRequestURI().contains("/resource/")) {
            chain.doFilter(request, response);
            return;
        }
        
        // 4. ?��?��?�� ?��?��?���? ?��?? resource ?���??�� 경우 => 로직 처리 ?��?�� ?��?�� ?��?���? ?��?��
        if (request.getRequestURI().contains("/noAuth/")) {
            chain.doFilter(request, response);
            return;
        }

        // 5. OPTIONS ?���??�� 경우 => 로직 처리 ?��?�� ?��?�� ?��?���? ?��?��
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }
        
        try {
        	
	        // [STEP 1] Client?��?�� API�? ?���??�� ?�� Header?�� AccessToken�? RefreshToken?�� ?��?��?��?��?��.
	        String headerAT = request.getHeader(AuthConstants.AUTH_HEADER);
	        String headerRT = request.getHeader(AuthConstants.AUTH_RF_TOKEN);
	        
	        // [STEP 2] Header ?��?�� Access ?��근을 추출?��?��?��.
	        String tokenAT = TokenUtils.getTokenFromHeader(headerAT);
	        
	        // [STEP 3] Redis?��?�� Access ?��?�� 값으�? BlackList 조회
	        String blackList = redisService.getValues(tokenAT);
	        
	        if(blackList != null && !blackList.equals("")) {  
	        	throw new BusinessExceptionHandler(ErrorCode.RELOGIN_TOKEN_ERROR.getMessage(), ErrorCode.RELOGIN_TOKEN_ERROR);
	        }
	        
	        // [STEP 4] Header?�� Access Token�? ?��?�� 경우, Access Token?�� ???�� ?��증만 ?��?��?���? API Response 진행?��?��?��.
	        if(headerRT == null) {
	        	
	        	// [STEP 5] 추출?�� Access ?��?��?�� ?��?��?���? ?���?�? 체크?��?��?��.
	        	if(TokenUtils.isValidToken(tokenAT)) {
	        		
	        		// [STEP 5-1] ?��?��?�� 기반?���? ?��?��?�� ?��?��?���? 반환 받습?��?��.
	        		String userId = TokenUtils.getUserIdFromToken(tokenAT, "userId");
	        		log.debug("userId Check : " + userId);
	        		
	        		// [STEP 5-2] ?��?��?�� ?��?��?���? 존재?��?���? ?���?�? 체크?��?��?��.
					if (userId != null && !userId.equalsIgnoreCase("")) {
						chain.doFilter(request, response);
					} else { // ?��?��?�� ?��?��?���? 존재?���? ?��?�� 경우
						throw new BusinessExceptionHandler(ErrorCode.RELOGIN_TOKEN_ERROR.getMessage(), ErrorCode.RELOGIN_TOKEN_ERROR);
					}
					
	        	}else { // Access Token?�� ?��?��?���? ?��?�� 경우
	        		throw new BusinessExceptionHandler(ErrorCode.RELOGIN_TOKEN_ERROR.getMessage(), ErrorCode.RELOGIN_TOKEN_ERROR);
	        	}
	        	
	        	
	        }else { // [STEP 6] Header?�� Refresh Token?�� ?��?�� 경우, Refresh Token?�� ???�� ?���? ?��?��
	        	
	        	// [STEP 6-1] Header ?��?�� ?��근을 추출?��?��?��.
	        	String tokenRT = TokenUtils.getTokenFromHeader(headerRT);
	            
	        	// [STEP 7] 추출?�� Refresh ?��근이 ?��?��?���? ?���?�? 체크?��?��?��.
	        	if(TokenUtils.isValidToken(tokenRT)) {
	
	        		// [STEP 7-1] Access Token?��?�� userId 추출?��?��?��.
	        		String userId = commonService.getJsonData(TokenUtils.getTokenPayload(tokenAT), "userId");
	        		
	        		// [STEP 7-2] Redis?��?�� userId�? Refresh ?��?�� 추출?��?��?��.
	        		String tokenRTFromRedis = redisService.getValues(userId);
	        		
	        		// [STEP 8] Header ?��?�� Refresh ?��?���? Redis?��?�� 조회?��?�� Refresh ?��?�� 비교?��?�� 같을 경우 Access ?��?��?�� ?��발급 ?��?��?��.
	        		if(tokenRT.equals(tokenRTFromRedis)) {
	        			
	        			// [STEP 8-1] Access ?��?��?�� ?��발급?��?�� response Header?�� ?���? API Response 진행
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
	        			
	        		}else { // Refresh ?��?�� 비교 ?�� 값이 같�? ?��?�� 경우 ?�� 로그?�� ?���? 
	        			throw new BusinessExceptionHandler(ErrorCode.RELOGIN_TOKEN_ERROR.getMessage(), ErrorCode.RELOGIN_TOKEN_ERROR);
	        		}
	        		
	        	}else { // Refresh ?��?��?�� ?��?��?���? ?��?�� 경우, ?�� 로그?�� ?���?
	        		throw new BusinessExceptionHandler(ErrorCode.RELOGIN_TOKEN_ERROR.getMessage(), ErrorCode.RELOGIN_TOKEN_ERROR);
	        	}
	        	
	        }

            
        } catch (Exception e) {
            // Token ?��?�� Exception?�� 발생 ?��???�� 경우 => ?��?��?��?��?��?�� ?��?��값을 반환?���? 종료?��?��?��.
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
     * ?��?�� �??�� Exception 발생 ?�� ?��?�� ?��?���? 구성
     *
     * @param e Exception
     * @return JSONObject
     */
    private JSONObject jsonResponseWrapper(Exception e) {

        String resultMsg = "";
        // JWT ?��?�� 만료
        if (e instanceof ExpiredJwtException) {
            resultMsg = "TOKEN Expired";
        }
        // JWT ?��?��?�� ?��?��?�� ?��?��
        else if (e instanceof SignatureException) {
            resultMsg = "TOKEN SignatureException Login";
        }
        // JWT ?��?��?��?��?�� ?���? 발생 ?��
        else if (e instanceof JwtException) {
            resultMsg = "TOKEN Parsing JwtException";
        }
        // ?��?��?�� BusinessException ?��류일 경우
        else if (e instanceof BusinessExceptionHandler) 
        {
            resultMsg = e.getMessage();
         // ?��?�� JTW ?��?��?��?��?�� ?���? 발생
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