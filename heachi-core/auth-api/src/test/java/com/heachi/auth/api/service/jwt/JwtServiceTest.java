package com.heachi.auth.api.service.jwt;

import com.heachi.admin.common.exception.jwt.JwtException;
import com.heachi.auth.TestConfig;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.mysql.define.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtServiceTest extends TestConfig {

    @Autowired JwtService jwtService;
    @Autowired UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("token에 등록되지 않은 값을 넣으면 오류가 발생한다.")
    void isValidTokenMalformed() {
        // given
        String malformedToken = "sjaklfjslfjlsajvsaadsf";
        User user = User.builder()
                .name("김민수")
                .email("kimminsu@dankook.ac.kr")
                .build();
        User savedUser = userRepository.save(user);

        // when
        assertThatThrownBy(() -> jwtService.isTokenValid(malformedToken, savedUser.getUsername()))
                .isInstanceOf(MalformedJwtException.class); // then
    }

    @Test
    @DisplayName("token의 유효시간이 지났으면 오류가 발생한다.")
    void tokenExpiredTest() {
        // given
        User user = User.builder()
                .name("김민수")
                .email("kimminsu@dankook.ac.kr")
                .build();
        User savedUser = userRepository.save(user);

        // when
        String expiredToken = jwtService.generateAccessToken(new HashMap<>(), user, new Date());

        // then
        assertThatThrownBy(() -> jwtService.isTokenValid(expiredToken, savedUser.getUsername()))
                .isInstanceOf(ExpiredJwtException.class);

    }

    @Test
    @DisplayName("jwt 토큰을 생성한다.")
    void provideToken() {
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

        // when
        String token = jwtService.generateAccessToken(map, savedUser);

        // then
        System.out.println("token = " + token);
        boolean result = jwtService.isTokenValid(token, savedUser.getUsername());
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("jwt 토큰에 Claims를 넣어서 생성한다.")
    void provideTokenIncludeClaims() {
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

        // when
        String token = jwtService.generateAccessToken(map, savedUser);
        Claims claims = jwtService.extractAllClaims(token);


        // then
        boolean result = jwtService.isTokenValid(token, savedUser.getUsername());
        assertThat(result).isTrue();
        assertAll(
                () -> assertThat(claims.getSubject()).isEqualTo("kimminsu@dankook.ac.kr"),
                () -> assertThat(claims.get("role")).isEqualTo(UserRole.USER.name()),
                () -> assertThat(claims.get("name")).isEqualTo("김민수"),
                () -> assertThat(claims.get("profileImageUrl")).isEqualTo("https://google.com")
        );
    }

    @Test
    @DisplayName("jwt 토큰의 Claims에 role, name, profileImageUrl 키가 없다면, 오류를 발생시킨다.")
    void provideTokenClaimsOmitElement() {
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
//        map.put("name", savedUser.getName());
        map.put("profileImageUrl", savedUser.getProfileImageUrl());

        // when
        String token = jwtService.generateAccessToken(map, savedUser);


        // then
        boolean result = jwtService.isTokenValid(token, savedUser.getUsername());
        assertThat(result).isFalse();
    }

//    @Test
//    @DisplayName("존재하지 않는 UserRole을 넣었을때 오류 발생")
//    void notexistUserRoleException() {
//        // given
//        User user = User.builder()
//                .name("김민수")
//                .role(null)
//                .email("kimminsu@dankook.ac.kr")
//                .profileImageUrl("https://google.com")
//                .build();
//        User savedUser = userRepository.save(user);
//
//        HashMap<String, String> map = new HashMap<>();
//        map.put("role", null);
//        map.put("name", savedUser.getName());
//        map.put("profileImageUrl", savedUser.getProfileImageUrl());
//        String token = jwtService.generateToken(map, savedUser);
//
//        // when
//        assertThatThrownBy(() -> jwtService.isTokenValid(token, savedUser.getUsername()))
//                // then
//                .isInstanceOf(JwtException.class);
//
//
//    }
}