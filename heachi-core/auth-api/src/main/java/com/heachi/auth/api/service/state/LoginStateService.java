package com.heachi.auth.api.service.state;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.state.LoginStateException;
import com.heachi.redis.define.state.LoginState;
import com.heachi.redis.define.state.repository.LoginStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginStateService {

    private final LoginStateRepository loginStateRepository;

    // Login State 생성
    public String generateLoginState() {
        LoginState loginState = LoginState.builder()
                .isUse(true)
                .build();
        LoginState savedLoginState = loginStateRepository.save(loginState);
        log.info(">>>> Generated Login State : {}", savedLoginState);

        return savedLoginState.getState().toString();
    }

    // Login State 검증
    public boolean isValidLoginState(String loginState) {
        UUID uuid = UUID.fromString(loginState);
        LoginState findLoginState = loginStateRepository.findById(uuid)
                .orElseThrow(() -> {
                    log.warn(">>>> {} : {}", loginState, ExceptionMessage.LOGINSTATE_NOT_FOUND);
                    throw new LoginStateException(ExceptionMessage.LOGINSTATE_NOT_FOUND);
                });

        if (!findLoginState.isUse()) {
            log.warn(">>>> {} : {}", loginState, ExceptionMessage.LOGINSTATE_IS_NOT_USE);
            throw new LoginStateException(ExceptionMessage.LOGINSTATE_IS_NOT_USE);
        }

        loginStateRepository.deleteById(uuid); // 사용한 LoginState는 삭제

        return true;
    }
}
