package com.heachi.auth.api.service.state;

import com.heachi.admin.common.exception.state.LoginStateException;
import com.heachi.auth.TestConfig;
import com.heachi.redis.define.state.LoginState;
import com.heachi.redis.define.state.LoginStateRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginStateServiceTest extends TestConfig {

    @Autowired
    private LoginStateService loginStateService;

    @Autowired
    private LoginStateRepository loginStateRepository;

    @Test
    @DisplayName("LoginState를 생성하면 redis에 저장되고, uuid가 String형태로 출력된다.")
    void redisLoginStateGenerate() {
        // given
        String loginState = loginStateService.generateLoginState();

        // when
        Optional<LoginState> findLoginState = loginStateRepository.findById(UUID.fromString(loginState));

        // then
        assertThat(findLoginState.get().getState().toString()).isEqualTo(loginState);
        assertThat(findLoginState.get().isUse()).isTrue();
    }

    @Test
    @DisplayName("올바른 LoginState를 요청하면, 검증이 완료되고, redis에서 삭제된다.")
    void validLoginStateAndRedisEntityDelete() {
        // given
        String loginState = loginStateService.generateLoginState();

        // when
        boolean validLoginState = loginStateService.isValidLoginState(loginState);
        Optional<LoginState> findLoginState = loginStateRepository.findById(UUID.fromString(loginState));

        // then
        assertThat(validLoginState).isTrue();
        assertThat(findLoginState.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("존재하지 않는 loginState로 요청하면, 찾을 수 없다는 에러를 발생시킨다.")
    void throwNotFoundExceptionWhenRedisNotExistLoginState() {
        // given
        String loginState = UUID.randomUUID().toString();

        // when
        assertThrows(LoginStateException.class,
                () -> loginStateService.isValidLoginState(loginState));
    }

    @Test
    @DisplayName("loginState의 isUse가 true가 아니라면, 사용할 수 없으므로 에러를 발생시킨다.")
    void throwIsNotUseExceptionWhenLoginStateIsUseIsNotTrue() {
        // given
        LoginState loginState = loginStateRepository.save(LoginState.builder()
                .isUse(false)
                .build());

        // when
        assertThrows(LoginStateException.class,
                () -> loginStateService.isValidLoginState(loginState.getState().toString()));
    }
}