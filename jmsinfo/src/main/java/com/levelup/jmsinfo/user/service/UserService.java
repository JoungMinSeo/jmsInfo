package com.levelup.jmsinfo.user.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.levelup.jmsinfo.user.vo.UserVO;

/**
 * @purpose ?¬?©? κ΄?? ¨ service
 * 
 * @  ?? ?Ό          ?? ?       ?? ?΄?©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.17       ? λ―Όμ       μ΅μ΄??± 
 * @ 2023.02.22       ? λ―Όμ       ?¬?©? κΆν μΆκ? 
 *
 * @author ? λ―Όμ
 * @since  2023.02.17
 *
 */
public interface UserService {

	Optional<UserVO> loadUserByUsername(String userId);
	
	int insertUser(Map<String, Object> param);
	
	List<String> loadUserByAuth(String userId);
	
}
