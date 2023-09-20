package com.heachi.redis.define.refreshToken.repository;

import com.heachi.redis.define.refreshToken.RefreshToken;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RefreshTokenRepositoryTest {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @AfterEach
    void tearDown() {
        refreshTokenRepository.deleteAll();
    }

    @Test
    @DisplayName("RefreshToken을 저장할 수 있다.")
    void redisLoginStateSave() {
        // given
        RefreshToken saveToken = refreshTokenRepository.save(RefreshToken.builder().refreshToken("testToken").email("test@naver.com").build());

        // when
        RefreshToken refreshToken = refreshTokenRepository.findById(saveToken.getRefreshToken()).get();

        // then
        assertThat(refreshToken.getRefreshToken()).isEqualTo(saveToken.getRefreshToken());
        assertThat(refreshToken.getEmail()).isEqualTo(saveToken.getEmail());
    }

    @Test
    @DisplayName("RefreshToken을 삭제할 수 있다.")
    void redisLoginStateDelete() {
        // given
        RefreshToken saveToken = refreshTokenRepository.save(RefreshToken.builder().refreshToken("testToken").email("test@naver.com").build());
        refreshTokenRepository.deleteById(saveToken.getRefreshToken());

        // when
        Optional<RefreshToken> findToken = refreshTokenRepository.findById(saveToken.getRefreshToken());

        // then
        assertThat(findToken.isEmpty()).isTrue();
    }
}