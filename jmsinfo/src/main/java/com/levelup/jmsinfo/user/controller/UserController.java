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
 * @purpose ?¬?©? κ΄?? ¨ Controller
 * 
 * @  ?? ?Ό            ?? ?        ?? ?΄?©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.17       ? λ―Όμ        μ΅μ΄??±
 * @ 2023.02.24       ? λ―Όμ        λ‘κ·Έ?? λ‘μ§ μΆκ? 
 *
 * @author ? λ―Όμ
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
     * ??κ°?? ??΄μ§? ?΄?
     * @return /user/signUp.jsp
     */
	@GetMapping("/noAuth/signUpPage")
	public String signUpPage() {
		return "/user/signUp";
	}
	
	/**
     * ??κ°??
     *
     * @param param : ??κ°?? ? λ³?
     * @return ResponseEntity : ??°?΄?Έ ? ?°?΄?° ? κ°?? 
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
     * λ‘κ·Έ??
     *
     * @param Authorization : Access ? ?° ? λ³?
     * @return ResponseEntity : ??°?΄?Έ ? ?°?΄?° ? κ°?? 
     */
	@PostMapping("/user/logout")
	public ResponseEntity<ApiResponse<?>> logout(@RequestHeader(value="Authorization") String accessToken){
		
		// [STEP 1] Header ?΄? ? κ·Όμ μΆμΆ?©??€.
		String tokenAT = TokenUtils.getTokenFromHeader(accessToken);
		
		// [STEP 2] Access ? ?°?? userId μ‘°ν
		String userId = commonService.getJsonData(TokenUtils.getTokenPayload(tokenAT), "userId");
		
		// [STEP 3] Access Token ? ?¨?μ§? κ²??¬ 
		if(TokenUtils.isValidToken(tokenAT)) { 
			// [STEP 3-1] Access ? ?° ? ?¨ ? Redis BlackList ?±λ‘?
			// TODO : Time ?κ°? λ§μΆκΈ?
			redisService.setValues(tokenAT, userId, Duration.ofMinutes(3));
		}
		
		// [STEP 4] Refresh ? ?° ?­? 
		redisService.delValues(userId);
		
		ApiResponse<?> ar = ApiResponse.builder()
				.result(null)
				.resultCode(SuccessCode.LOGOUT_SUCCESS.getStatus())
				.resultMsg(SuccessCode.LOGOUT_SUCCESS.getMessage())
				.build();
		
		return new ResponseEntity<>(ar, HttpStatus.OK);
	}
	
	
	
}
