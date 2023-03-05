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
 * @purpose ?��?��?�� �??�� Controller
 * 
 * @  ?��?��?��            ?��?��?��        ?��?��?��?��
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.17       ?��민서        최초?��?��
 * @ 2023.02.24       ?��민서        로그?��?�� 로직 추�? 
 *
 * @author ?��민서
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
     * ?��?���??�� ?��?���? ?��?��
     * @return /user/signUp.jsp
     */
	@GetMapping("/noAuth/signUpPage")
	public String signUpPage() {
		return "/user/signUp";
	}
	
	/**
     * ?��?���??��
     *
     * @param param : ?��?���??�� ?���?
     * @return ResponseEntity : ?��?��?��?�� ?�� ?��?��?�� ?�� �??�� 
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
     * 로그?��?��
     *
     * @param Authorization : Access ?��?�� ?���?
     * @return ResponseEntity : ?��?��?��?�� ?�� ?��?��?�� ?�� �??�� 
     */
	@PostMapping("/user/logout")
	public ResponseEntity<ApiResponse<?>> logout(@RequestHeader(value="Authorization") String accessToken){
		
		// [STEP 1] Header ?��?�� ?��근을 추출?��?��?��.
		String tokenAT = TokenUtils.getTokenFromHeader(accessToken);
		
		// [STEP 2] Access ?��?��?��?�� userId 조회
		String userId = commonService.getJsonData(TokenUtils.getTokenPayload(tokenAT), "userId");
		
		// [STEP 3] Access Token ?��?��?���? �??�� 
		if(TokenUtils.isValidToken(tokenAT)) { 
			// [STEP 3-1] Access ?��?�� ?��?�� ?�� Redis BlackList ?���?
			// TODO : Time ?���? 맞추�?
			redisService.setValues(tokenAT, userId, Duration.ofMinutes(3));
		}
		
		// [STEP 4] Refresh ?��?�� ?��?��
		redisService.delValues(userId);
		
		ApiResponse<?> ar = ApiResponse.builder()
				.result(null)
				.resultCode(SuccessCode.LOGOUT_SUCCESS.getStatus())
				.resultMsg(SuccessCode.LOGOUT_SUCCESS.getMessage())
				.build();
		
		return new ResponseEntity<>(ar, HttpStatus.OK);
	}
	
	
	
}
