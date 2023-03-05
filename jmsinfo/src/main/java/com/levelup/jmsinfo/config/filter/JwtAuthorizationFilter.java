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
 * @purpose ì§?? •?•œ URL ë³? JWT ?œ ?š¨?„± ê²?ì¦ì„ ?ˆ˜?–‰?•˜ë©? ì§ì ‘? ?¸ ?‚¬?š©? '?¸ì¦?'?„ ?™•?¸
 * 
 * @  ?ˆ˜? •?¼            ?ˆ˜? •?      ?ˆ˜? •?‚´?š©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.09       ? •ë¯¼ì„œ      ìµœì´ˆ?ƒ?„±
 * @ 2023.02.24       ? •ë¯¼ì„œ      filter ì¡°ê±´ ë³?ê²?
 * @ 2023.02.28       ? •ë¯¼ì„œ      Access ?† ?° ?¬ë°œê¸‰ ì¶”ê?
 *
 * @author ? •ë¯¼ì„œ
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
    	
        // 1. ?† ?°?´ ?•„?š”?•˜ì§? ?•Š?? API URL?— ???•´?„œ ë°°ì—´ë¡? êµ¬ì„±?•©?‹ˆ?‹¤.
        List<String> list = Arrays.asList(
        		"/",
                "/user/login",
                "/favicon.ico",
                "/user/insertUser",
                "/user/logout",
                "/noAuth/agreementPage"
        );

        // 2. ?† ?°?´ ?•„?š”?•˜ì§? ?•Š?? API URL?˜ ê²½ìš° => ë¡œì§ ì²˜ë¦¬ ?—†?´ ?‹¤?Œ ?•„?„°ë¡? ?´?™
        if (list.contains(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }
        
        // 3. ?† ?°?´ ?•„?š”?•˜ì§? ?•Š?? resource ?š”ì²??˜ ê²½ìš° => ë¡œì§ ì²˜ë¦¬ ?—†?´ ?‹¤?Œ ?•„?„°ë¡? ?´?™
        if (request.getRequestURI().contains("/resource/")) {
            chain.doFilter(request, response);
            return;
        }
        
        // 4. ?† ?°?´ ?•„?š”?•˜ì§? ?•Š?? resource ?š”ì²??˜ ê²½ìš° => ë¡œì§ ì²˜ë¦¬ ?—†?´ ?‹¤?Œ ?•„?„°ë¡? ?´?™
        if (request.getRequestURI().contains("/noAuth/")) {
            chain.doFilter(request, response);
            return;
        }

        // 5. OPTIONS ?š”ì²??¼ ê²½ìš° => ë¡œì§ ì²˜ë¦¬ ?—†?´ ?‹¤?Œ ?•„?„°ë¡? ?´?™
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            chain.doFilter(request, response);
            return;
        }
        
        try {
        	
	        // [STEP 1] Client?—?„œ APIë¥? ?š”ì²??•  ?•Œ Header?— AccessTokenê³? RefreshToken?„ ?™•?¸?•©?‹ˆ?‹¤.
	        String headerAT = request.getHeader(AuthConstants.AUTH_HEADER);
	        String headerRT = request.getHeader(AuthConstants.AUTH_RF_TOKEN);
	        
	        // [STEP 2] Header ?‚´?— Access ?† ê·¼ì„ ì¶”ì¶œ?•©?‹ˆ?‹¤.
	        String tokenAT = TokenUtils.getTokenFromHeader(headerAT);
	        
	        // [STEP 3] Redis?—?„œ Access ?† ?° ê°’ìœ¼ë¡? BlackList ì¡°íšŒ
	        String blackList = redisService.getValues(tokenAT);
	        
	        if(blackList != null && !blackList.equals("")) {  
	        	throw new BusinessExceptionHandler(ErrorCode.RELOGIN_TOKEN_ERROR.getMessage(), ErrorCode.RELOGIN_TOKEN_ERROR);
	        }
	        
	        // [STEP 4] Header?— Access Tokenë§? ?ˆ?„ ê²½ìš°, Access Token?— ???•œ ?¸ì¦ë§Œ ?ˆ˜?–‰?•˜ê³? API Response ì§„í–‰?•©?‹ˆ?‹¤.
	        if(headerRT == null) {
	        	
	        	// [STEP 5] ì¶”ì¶œ?•œ Access ?† ?°?´ ?œ ?š¨?•œì§? ?—¬ë¶?ë¥? ì²´í¬?•©?‹ˆ?‹¤.
	        	if(TokenUtils.isValidToken(tokenAT)) {
	        		
	        		// [STEP 5-1] ?† ?°?„ ê¸°ë°˜?œ¼ë¡? ?‚¬?š©? ?•„?´?“œë¥? ë°˜í™˜ ë°›ìŠµ?‹ˆ?‹¤.
	        		String userId = TokenUtils.getUserIdFromToken(tokenAT, "userId");
	        		log.debug("userId Check : " + userId);
	        		
	        		// [STEP 5-2] ?‚¬?š©? ?•„?´?””ê°? ì¡´ì¬?•˜?Š”ì§? ?—¬ë¶?ë¥? ì²´í¬?•©?‹ˆ?‹¤.
					if (userId != null && !userId.equalsIgnoreCase("")) {
						chain.doFilter(request, response);
					} else { // ?‚¬?š©? ?•„?´?””ê°? ì¡´ì¬?•˜ì§? ?•Š?„ ê²½ìš°
						throw new BusinessExceptionHandler(ErrorCode.RELOGIN_TOKEN_ERROR.getMessage(), ErrorCode.RELOGIN_TOKEN_ERROR);
					}
					
	        	}else { // Access Token?´ ?œ ?š¨?•˜ì§? ?•Š?„ ê²½ìš°
	        		throw new BusinessExceptionHandler(ErrorCode.RELOGIN_TOKEN_ERROR.getMessage(), ErrorCode.RELOGIN_TOKEN_ERROR);
	        	}
	        	
	        	
	        }else { // [STEP 6] Header?— Refresh Token?´ ?ˆ?„ ê²½ìš°, Refresh Token?— ???•œ ?¸ì¦? ?ˆ˜?–‰
	        	
	        	// [STEP 6-1] Header ?‚´?— ?† ê·¼ì„ ì¶”ì¶œ?•©?‹ˆ?‹¤.
	        	String tokenRT = TokenUtils.getTokenFromHeader(headerRT);
	            
	        	// [STEP 7] ì¶”ì¶œ?•œ Refresh ?† ê·¼ì´ ?œ ?š¨?•œì§? ?—¬ë¶?ë¥? ì²´í¬?•©?‹ˆ?‹¤.
	        	if(TokenUtils.isValidToken(tokenRT)) {
	
	        		// [STEP 7-1] Access Token?—?„œ userId ì¶”ì¶œ?•©?‹ˆ?‹¤.
	        		String userId = commonService.getJsonData(TokenUtils.getTokenPayload(tokenAT), "userId");
	        		
	        		// [STEP 7-2] Redis?—?„œ userIdë¡? Refresh ?† ?° ì¶”ì¶œ?•©?‹ˆ?‹¤.
	        		String tokenRTFromRedis = redisService.getValues(userId);
	        		
	        		// [STEP 8] Header ?‚´?˜ Refresh ?† ?°ê³? Redis?—?„œ ì¡°íšŒ?•´?˜¨ Refresh ?† ?° ë¹„êµ?•˜?—¬ ê°™ì„ ê²½ìš° Access ?† ?°?„ ?¬ë°œê¸‰ ?•©?‹ˆ?‹¤.
	        		if(tokenRT.equals(tokenRTFromRedis)) {
	        			
	        			// [STEP 8-1] Access ?† ?°?„ ?¬ë°œê¸‰?•˜?—¬ response Header?— ?‹´ê³? API Response ì§„í–‰
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
	        			
	        		}else { // Refresh ?† ?° ë¹„êµ ?›„ ê°’ì´ ê°™ì? ?•Š?„ ê²½ìš° ?¬ ë¡œê·¸?¸ ?š”ì²? 
	        			throw new BusinessExceptionHandler(ErrorCode.RELOGIN_TOKEN_ERROR.getMessage(), ErrorCode.RELOGIN_TOKEN_ERROR);
	        		}
	        		
	        	}else { // Refresh ?† ?°?´ ?œ ?š¨?•˜ì§? ?•Š?„ ê²½ìš°, ?¬ ë¡œê·¸?¸ ?š”ì²?
	        		throw new BusinessExceptionHandler(ErrorCode.RELOGIN_TOKEN_ERROR.getMessage(), ErrorCode.RELOGIN_TOKEN_ERROR);
	        	}
	        	
	        }

            
        } catch (Exception e) {
            // Token ?‚´?— Exception?´ ë°œìƒ ?•˜???„ ê²½ìš° => ?´?¼?´?–¸?Š¸?— ?‘?‹µê°’ì„ ë°˜í™˜?•˜ê³? ì¢…ë£Œ?•©?‹ˆ?‹¤.
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
     * ?† ?° ê´?? ¨ Exception ë°œìƒ ?‹œ ?˜ˆ?™¸ ?‘?‹µê°? êµ¬ì„±
     *
     * @param e Exception
     * @return JSONObject
     */
    private JSONObject jsonResponseWrapper(Exception e) {

        String resultMsg = "";
        // JWT ?† ?° ë§Œë£Œ
        if (e instanceof ExpiredJwtException) {
            resultMsg = "TOKEN Expired";
        }
        // JWT ?—ˆ?š©?œ ?† ?°?´ ?•„?‹˜
        else if (e instanceof SignatureException) {
            resultMsg = "TOKEN SignatureException Login";
        }
        // JWT ?† ?°?‚´?—?„œ ?˜¤ë¥? ë°œìƒ ?‹œ
        else if (e instanceof JwtException) {
            resultMsg = "TOKEN Parsing JwtException";
        }
        // ?„¤? •?•œ BusinessException ?˜¤ë¥˜ì¼ ê²½ìš°
        else if (e instanceof BusinessExceptionHandler) 
        {
            resultMsg = e.getMessage();
         // ?´?™¸ JTW ?† ?°?‚´?—?„œ ?˜¤ë¥? ë°œìƒ
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