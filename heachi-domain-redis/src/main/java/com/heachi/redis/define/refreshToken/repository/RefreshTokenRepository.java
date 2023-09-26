package com.heachi.redis.define.refreshToken.repository;

import com.heachi.redis.define.refreshToken.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
