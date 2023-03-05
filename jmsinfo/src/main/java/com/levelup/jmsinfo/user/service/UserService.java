package com.levelup.jmsinfo.user.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.levelup.jmsinfo.user.vo.UserVO;

/**
 * @purpose ?‚¬?š©? ê´?? ¨ service
 * 
 * @  ?ˆ˜? •?¼          ?ˆ˜? •?       ?ˆ˜? •?‚´?š©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.17       ? •ë¯¼ì„œ       ìµœì´ˆ?ƒ?„± 
 * @ 2023.02.22       ? •ë¯¼ì„œ       ?‚¬?š©? ê¶Œí•œ ì¶”ê? 
 *
 * @author ? •ë¯¼ì„œ
 * @since  2023.02.17
 *
 */
public interface UserService {

	Optional<UserVO> loadUserByUsername(String userId);
	
	int insertUser(Map<String, Object> param);
	
	List<String> loadUserByAuth(String userId);
	
}
