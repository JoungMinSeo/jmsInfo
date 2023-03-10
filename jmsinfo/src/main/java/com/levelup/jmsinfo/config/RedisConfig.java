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
 * @purpose Redis ?€?  λ°? κ΄?? ¨ λ©μ?
 * 
 * @  ?? ?Ό          ?? ?       ?? ?΄?©
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.22       ? λ―Όμ       μ΅μ΄??±
 *
 * @author ? λ―Όμ
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
	 * RedisConnectionFactory ?Έ?°??΄?€λ₯? ?΅?΄ LettuceConnectionFactoryλ₯? ??±??¬ λ°ν
	 */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }


    /**
	 * setKeySerializer, setValueSerializer ?€? 
     * redis-cli? ?΅?΄ μ§μ  ?°?΄?°λ₯? μ‘°ν ? ??λ³? ? ?? ??λ‘? μΆλ ₯?? κ²μ λ°©μ? 
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
