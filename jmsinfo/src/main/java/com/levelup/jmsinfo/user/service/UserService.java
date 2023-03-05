package com.levelup.jmsinfo.user.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.levelup.jmsinfo.user.vo.UserVO;

/**
 * @purpose ?��?��?�� �??�� service
 * 
 * @  ?��?��?��          ?��?��?��       ?��?��?��?��
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.17       ?��민서       최초?��?�� 
 * @ 2023.02.22       ?��민서       ?��?��?�� 권한 추�? 
 *
 * @author ?��민서
 * @since  2023.02.17
 *
 */
public interface UserService {

	Optional<UserVO> loadUserByUsername(String userId);
	
	int insertUser(Map<String, Object> param);
	
	List<String> loadUserByAuth(String userId);
	
}
