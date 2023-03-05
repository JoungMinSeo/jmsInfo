package com.levelup.jmsinfo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

/**
 * @purpose Redis ?„¤? • ë°? ê´?? ¨ ë©”ì†Œ?“œ
 * 
 * @  ?ˆ˜? •?¼          ?ˆ˜? •?       ?ˆ˜? •?‚´?š©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.22       ? •ë¯¼ì„œ       ìµœì´ˆ?ƒ?„±
 *
 * @author ? •ë¯¼ì„œ
 * @since  2023.02.22
 *
 */
@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories
public class RedisConfig {

	@Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    /**
	 * RedisConnectionFactory ?¸?„°?˜?´?Š¤ë¥? ?†µ?•´ LettuceConnectionFactoryë¥? ?ƒ?„±?•˜?—¬ ë°˜í™˜
	 */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }


    /**
	 * setKeySerializer, setValueSerializer ?„¤? •
     * redis-cli?„ ?†µ?•´ ì§ì ‘ ?°?´?„°ë¥? ì¡°íšŒ ?‹œ ?•Œ?•„ë³? ?ˆ˜ ?—†?Š” ?˜•?ƒœë¡? ì¶œë ¥?˜?Š” ê²ƒì„ ë°©ì? 
	 */
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
	
}
