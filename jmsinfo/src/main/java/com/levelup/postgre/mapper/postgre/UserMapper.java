package com.levelup.postgre.mapper.postgre;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.levelup.jmsinfo.user.vo.UserVO;

public interface UserMapper {

	Optional<UserVO> loadUserByUsername(String userId);
	
	int insertUser(Map<String, Object> param);
	
	int insertAuth(Map<String, Object> param);
	
	List<String> loadUserByAuth(String userId);
	
}
