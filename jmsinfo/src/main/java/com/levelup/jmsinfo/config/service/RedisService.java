package com.levelup.jmsinfo.config.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * @purpose Redis �??�� 로직 
 * 
 * @  ?��?��?��            ?��?��?��        ?��?��?��?��
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.24       ?��민서        최초?��?��
 *
 * @author ?��민서
 * @since  2023.02.24
 *
 */
@Service
@RequiredArgsConstructor
public class RedisService {

	private final RedisTemplate redisTemplate;
	
	/**
     * Redis key - value ?��?�� 메소?��
     *
     * @param token  : Redis key
     * 		, userId : Redis value
     */
    public void setValues(String key, String value, Duration timeOut){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value, timeOut);  // 3�? ?�� 메모리에?�� ?��?��?��?��.
    }

    /**
     * Redis key 값으�? value 조회 메소?��
     *
     * @param token : 리프?��?�� ?��?�� (Redis ?�� �?)
     * @return String : Redis?��?�� 조회?��?�� value �?
     */
    public String getValues(String key){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    /**
     * Redis key 값으�? value ?��?�� 메소?��
     *
     * @param token : 리프?��?�� ?��?�� (Redis ?�� �?)
     */
    public void delValues(String key) {
        redisTemplate.delete(key);
    }
	
}
