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
 * @purpose Redis ?��?�� �? �??�� 메소?��
 * 
 * @  ?��?��?��          ?��?��?��       ?��?��?��?��
 * @ ---------       ---------   -------------------------------
 * @ 2023.02.22       ?��민서       최초?��?��
 *
 * @author ?��민서
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
	 * RedisConnectionFactory ?��?��?��?��?���? ?��?�� LettuceConnectionFactory�? ?��?��?��?�� 반환
	 */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);
    }


    /**
	 * setKeySerializer, setValueSerializer ?��?��
     * redis-cli?�� ?��?�� 직접 ?��?��?���? 조회 ?�� ?��?���? ?�� ?��?�� ?��?���? 출력?��?�� 것을 방�? 
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
