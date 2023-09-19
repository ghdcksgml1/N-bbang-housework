package com.heachi.redis.define.refreshToken.repository;

import com.heachi.redis.define.refreshToken.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class RefreshTokenRepository{
    private RedisTemplate<String, String> redisTemplate;

    public RefreshTokenRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRefreshToken(final RefreshToken refreshToken) {
        redisTemplate.opsForValue().set(
                refreshToken.getRefreshToken(),
                refreshToken.getEmail(),
                refreshToken.getExpiration(),
                TimeUnit.MICROSECONDS
        );
        log.info(">>>> Generated RefreshToken : {}", refreshToken.getEmail());
    }

    public String findByRefreshToken(String refreshToken) {
        return redisTemplate.opsForValue().get(refreshToken);
    }

    public void deleteRefreshToken(final String refreshToken) {
        redisTemplate.delete(refreshToken);
    }
}
