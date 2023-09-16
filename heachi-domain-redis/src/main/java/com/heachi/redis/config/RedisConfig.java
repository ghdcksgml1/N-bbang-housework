package com.heachi.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableRedisRepositories(basePackages = "com.heachi.redis")
@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    // Bean으로 등록해 Redis 연결 - Lettuce 사용
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    // RedisTemplate를 사용해 데이터 저장, 검색 ...
    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        // 나중에 필요한 자료구조로 Serialize 해서 사용할 예정
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        // 커넥션 등록
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }
}
