package com.levelup.jmsinfo.config.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * @purpose Redis κ΄?? ¨ λ‘μ§ 
 * 
 * @  ?? ?Ό            ?? ?        ?? ?΄?©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.24       ? λ―Όμ        μ΅μ΄??±
 *
 * @author ? λ―Όμ
 * @since  2023.02.24
 *
 */
@Service
@RequiredArgsConstructor
public class RedisService {

	private final RedisTemplate redisTemplate;
	
	/**
     * Redis key - value ?€?  λ©μ?
     *
     * @param token  : Redis key
     * 		, userId : Redis value
     */
    public void setValues(String key, String value, Duration timeOut){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, value, timeOut);  // 3λΆ? ?€ λ©λͺ¨λ¦¬μ? ?­? ??€.
    }

    /**
     * Redis key κ°μΌλ‘? value μ‘°ν λ©μ?
     *
     * @param token : λ¦¬ν? ? ? ?° (Redis ?€ κ°?)
     * @return String : Redis?? μ‘°ν?΄?¨ value κ°?
     */
    public String getValues(String key){
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    /**
     * Redis key κ°μΌλ‘? value ?­?  λ©μ?
     *
     * @param token : λ¦¬ν? ? ? ?° (Redis ?€ κ°?)
     */
    public void delValues(String key) {
        redisTemplate.delete(key);
    }
	
}
