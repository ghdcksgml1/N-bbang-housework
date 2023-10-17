package com.heachi.redis.define.state;

import com.heachi.redis.define.state.repository.LoginStateRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LoginStateRepositoryTest {
    @Autowired
    private LoginStateRepository loginStateRepository;

    @AfterEach
    void tearDown() {
        loginStateRepository.deleteAll();
    }

    @Test
    @DisplayName("LoginState를 저장할 수 있다.")
    void redisLoginStateSave() {
        // given
        LoginState savedEntity = loginStateRepository.save(LoginState.builder().isUse(true).build());

        // when
        Optional<LoginState> byId = loginStateRepository.findById(savedEntity.getState());

        // then
        assertThat(byId.get().isUse()).isTrue();
        assertThat(byId.get().getState()).isInstanceOf(UUID.class);
    }

    @Test
    @DisplayName("LoginState를 삭제할 수 있다.")
    void redisLoginStateDelete() {
        // given
        LoginState savedEntity = loginStateRepository.save(LoginState.builder().isUse(true).build());
        loginStateRepository.deleteById(savedEntity.getState());

        // when
        Optional<LoginState> findLoginState = loginStateRepository.findById(savedEntity.getState());

        // then
        assertThat(findLoginState.isEmpty()).isTrue();
    }
}