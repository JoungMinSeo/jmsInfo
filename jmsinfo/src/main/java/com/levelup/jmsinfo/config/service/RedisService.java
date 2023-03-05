package com.levelup.jmsinfo.config.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * @purpose Redis ê´?? ¨ ë¡œì§ 
 * 
 * @  ?ˆ˜? •?¼            ?ˆ˜? •?        ?ˆ˜? •?‚´?š©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.24       ? •ë¯¼ì„œ        ìµœì´ˆ?ƒ?„±
 *
 * @author ? •ë¯¼ì„œ
 * @since  2023.02.24
 *
 */
@Service
@RequiredArgsConstructor
public class RedisService {

	private final RedisTemplate redisTemplate;
	
	/**
     * Redis key - value ?„¤? • ë©”ì†Œ?“œ
     *
     * @param token  : Redis key
     * 		, userId : Redis value
     */
    public void setValues(String key, String value, Duration timeOut){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value, timeOut);  // 3ë¶? ?’¤ ë©”ëª¨ë¦¬ì—?„œ ?‚­? œ?œ?‹¤.
    }

    /**
     * Redis key ê°’ìœ¼ë¡? value ì¡°íšŒ ë©”ì†Œ?“œ
     *
     * @param token : ë¦¬í”„? ˆ?‹œ ?† ?° (Redis ?‚¤ ê°?)
     * @return String : Redis?—?„œ ì¡°íšŒ?•´?˜¨ value ê°?
     */
    public String getValues(String key){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    /**
     * Redis key ê°’ìœ¼ë¡? value ?‚­? œ ë©”ì†Œ?“œ
     *
     * @param token : ë¦¬í”„? ˆ?‹œ ?† ?° (Redis ?‚¤ ê°?)
     */
    public void delValues(String key) {
        redisTemplate.delete(key);
    }
	
}
