package com.heachi.auth.api.service.token;

import com.heachi.admin.common.exception.ExceptionMessage;
import com.heachi.admin.common.exception.jwt.JwtException;
import com.heachi.auth.api.service.jwt.JwtService;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.mysql.define.user.repository.UserRepository;
import com.heachi.redis.define.refreshToken.RefreshToken;
import com.heachi.redis.define.refreshToken.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class RefreshTokenServiceTest {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("Refresh Token 저장 & 조회 테스트")
    void redisRefreshTokenGenerate() {
        // given
        RefreshToken saveToken = RefreshToken.builder()
                .refreshToken("TestToken")
                .email("TestEmail@test.com")
                .build();

        refreshTokenService.saveRefreshToken(saveToken);

        // when
        Optional<RefreshToken> testToken = refreshTokenRepository.findById("TestToken");

        // then
        assertThat(testToken.get().getRefreshToken()).isEqualTo("TestToken");
        assertThat(testToken.get().getEmail()).isEqualTo("TestEmail@test.com");
    }


    @Test
    @DisplayName("Refresh Token 삭제 테스트 - 로그아웃시 Refresh Token이 Redis에서 삭제된다.")
    void redisRefreshTokenDeleteWhenLogout() {
        // given
        User user = User.builder()
                .name("김민수")
                .role(UserRole.USER)
                .email("kimminsu@dankook.ac.kr")
                .profileImageUrl("https://google.com")
                .build();
        User savedUser = userRepository.save(user);

        HashMap<String, String> map = new HashMap<>();
        map.put("role", savedUser.getRole().name());
        map.put("name", savedUser.getName());
        map.put("profileImageUrl", savedUser.getProfileImageUrl());
        String accessToken = jwtService.generateAccessToken(map, savedUser);
        String refreshToken = jwtService.generateRefreshToken(map, savedUser);

        refreshTokenService.saveRefreshToken(RefreshToken.builder()
                .refreshToken(refreshToken)
                .email(savedUser.getEmail())
                .build());

        refreshTokenService.logout(refreshToken);

        // when
        Optional<RefreshToken> findToken = refreshTokenRepository.findById(refreshToken);

        // then
        assertThat(findToken.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Redis에 존재하지 않는 Refresh Token으로 요청할 경우 예외가 발생한다.")
    void LogoutWhenRedisNotExistRefreshToken() {
        // given
        User user = User.builder()
                .name("김민수")
                .role(UserRole.USER)
                .email("kimminsu@dankook.ac.kr")
                .profileImageUrl("https://google.com")
                .build();
        User savedUser = userRepository.save(user);

        HashMap<String, String> map = new HashMap<>();
        map.put("role", savedUser.getRole().name());
        map.put("name", savedUser.getName());
        map.put("profileImageUrl", savedUser.getProfileImageUrl());
        String refreshToken = jwtService.generateRefreshToken(map, savedUser);

        refreshTokenService.saveRefreshToken(RefreshToken.builder()
                .refreshToken(refreshToken)
                .email(savedUser.getEmail())
                .build());

        // RTK 삭제
        refreshTokenRepository.deleteById(refreshToken);

        // when
        JwtException exception = assertThrows(JwtException.class,
                () -> refreshTokenService.logout(refreshToken));

        assertThat(exception.getMessage()).isEqualTo(ExceptionMessage.JWT_NOT_EXIST_RTK.getText());
    }

    @Test
    @DisplayName("유효하지 않은 Refresh Token으로 요청할 경우 예외가 발생한다.")
    void LogoutWhenRedisInvalidRefreshToken() {
        // given
        User user = User.builder()
                .name("김민수")
                .role(UserRole.USER)
                .email("kimminsu@dankook.ac.kr")
                .profileImageUrl("https://google.com")
                .build();
        User savedUser = userRepository.save(user);

        HashMap<String, String> map = new HashMap<>();
        map.put("role", savedUser.getRole().name());
        map.put("name", savedUser.getName());
        map.put("profileImageUrl", savedUser.getProfileImageUrl());
        String refreshToken = jwtService.generateRefreshToken(map, savedUser);

        refreshTokenService.saveRefreshToken(RefreshToken.builder()
                .refreshToken(refreshToken)
                .email("invalidEmail@naver.com")
                .build());

        // when
        JwtException exception = assertThrows(JwtException.class,
                () -> refreshTokenService.logout(refreshToken));

        assertThat(exception.getMessage()).isEqualTo(ExceptionMessage.JWT_INVALID_RTK.getText());
    }

}