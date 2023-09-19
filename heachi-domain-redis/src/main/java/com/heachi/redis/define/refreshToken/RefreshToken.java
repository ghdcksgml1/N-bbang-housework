package com.heachi.redis.define.refreshToken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 60*60*24*7) // 7일
public class RefreshToken {
    @Id
    private String refreshToken;

    @Indexed
    private String email;

    private long expiration;

    @Builder
    public RefreshToken(String refreshToken, String email, long expiration) {
        this.refreshToken = refreshToken;
        this.email = email;
        this.expiration = expiration;
    }
}