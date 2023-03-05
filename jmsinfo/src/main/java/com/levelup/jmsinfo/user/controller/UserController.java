package com.levelup.jmsinfo.user.controller;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import com.levelup.jmsinfo.cmmn.code.SuccessCode;
import com.levelup.jmsinfo.cmmn.response.ApiResponse;
import com.levelup.jmsinfo.config.TokenUtils;
import com.levelup.jmsinfo.config.service.CommonService;
import com.levelup.jmsinfo.config.service.RedisService;
import com.levelup.jmsinfo.user.service.UserService;

/**
 * @purpose ?‚¬?š©? ê´?? ¨ Controller
 * 
 * @  ?ˆ˜? •?¼            ?ˆ˜? •?        ?ˆ˜? •?‚´?š©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.17       ? •ë¯¼ì„œ        ìµœì´ˆ?ƒ?„±
 * @ 2023.02.24       ? •ë¯¼ì„œ        ë¡œê·¸?•„?›ƒ ë¡œì§ ì¶”ê? 
 *
 * @author ? •ë¯¼ì„œ
 * @since  2023.02.17
 *
 */
@Controller
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	RedisService redisService;
	
	/**
     * ?šŒ?›ê°??… ?˜?´ì§? ?´?™
     * @return /user/signUp.jsp
     */
	@GetMapping("/noAuth/signUpPage")
	public String signUpPage() {
		return "/user/signUp";
	}
	
	/**
     * ?šŒ?›ê°??…
     *
     * @param param : ?šŒ?›ê°??… ? •ë³?
     * @return ResponseEntity : ?—…?°?´?Š¸ ?œ ?°?´?„° ?–‰ ê°??ˆ˜ 
     */
	@PostMapping("/user/insertUser")
	@ResponseBody
	public ResponseEntity<ApiResponse<?>> insertUser(@RequestBody Map<String, Object> param) {
		
		int result = userService.insertUser(param);
		
		ApiResponse<?> ar = ApiResponse.builder()
				.result(result)
				.resultCode(SuccessCode.INSERT_SUCCESS.getStatus())
				.resultMsg(SuccessCode.INSERT_SUCCESS.getMessage())
				.build();
		
		return new ResponseEntity<>(ar, HttpStatus.OK);
	}
	
	/**
     * ë¡œê·¸?•„?›ƒ
     *
     * @param Authorization : Access ?† ?° ? •ë³?
     * @return ResponseEntity : ?—…?°?´?Š¸ ?œ ?°?´?„° ?–‰ ê°??ˆ˜ 
     */
	@PostMapping("/user/logout")
	public ResponseEntity<ApiResponse<?>> logout(@RequestHeader(value="Authorization") String accessToken){
		
		// [STEP 1] Header ?‚´?— ?† ê·¼ì„ ì¶”ì¶œ?•©?‹ˆ?‹¤.
		String tokenAT = TokenUtils.getTokenFromHeader(accessToken);
		
		// [STEP 2] Access ?† ?°?—?„œ userId ì¡°íšŒ
		String userId = commonService.getJsonData(TokenUtils.getTokenPayload(tokenAT), "userId");
		
		// [STEP 3] Access Token ?œ ?š¨?•œì§? ê²??‚¬ 
		if(TokenUtils.isValidToken(tokenAT)) { 
			// [STEP 3-1] Access ?† ?° ?œ ?š¨ ?‹œ Redis BlackList ?“±ë¡?
			// TODO : Time ?‹œê°? ë§ì¶”ê¸?
			redisService.setValues(tokenAT, userId, Duration.ofMinutes(3));
		}
		
		// [STEP 4] Refresh ?† ?° ?‚­? œ
		redisService.delValues(userId);
		
		ApiResponse<?> ar = ApiResponse.builder()
				.result(null)
				.resultCode(SuccessCode.LOGOUT_SUCCESS.getStatus())
				.resultMsg(SuccessCode.LOGOUT_SUCCESS.getMessage())
				.build();
		
		return new ResponseEntity<>(ar, HttpStatus.OK);
	}
	
	
	
}
