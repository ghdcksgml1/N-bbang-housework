package com.heachi.redis.define.state;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Getter
@ToString
@RedisHash(value = "state", timeToLive = 60 * 3) // 3분
public class LoginState {
    @Id
    private UUID state; // 검증하는 state

    private boolean isUse; // 사용 가능한지

    @Builder
    private LoginState(boolean isUse) {
        this.isUse = isUse;
    }
}
