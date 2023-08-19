package com.heachi.auth.api.service.auth;

import com.heachi.auth.TestConfig;
import com.heachi.auth.api.service.auth.response.AuthServiceLoginResponse;
import com.heachi.auth.api.service.jwt.JwtService;
import com.heachi.auth.api.service.oauth.OAuthService;
import com.heachi.auth.api.service.oauth.response.OAuthResponse;
import com.heachi.mysql.define.user.User;
import com.heachi.mysql.define.user.constant.UserPlatformType;
import com.heachi.mysql.define.user.constant.UserRole;
import com.heachi.mysql.define.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static com.heachi.mysql.define.user.constant.UserPlatformType.KAKAO;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthServiceTest extends TestConfig {

    @MockBean
    private OAuthService oAuthService;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("유저 플랫폼과 코드를 넘기면, 로그인이 성공하고, JWT 토큰이 정상적으로 발행된다.")
    void loginSuccess() throws Exception {
        // given
        UserPlatformType platformType = UserPlatformType.KAKAO;
        String code = "임의의 코드입니다.";
        String state = "임의의 state입니다.";
        String email = "kimminsu@dankook.ac.kr";

        User user = User.builder()
                .name("김민수")
                .email(email)
                .role(UserRole.USER)
                .build();
        userRepository.save(user);

        OAuthResponse oAuthResponse = OAuthResponse.builder()
                .platformId("123")
                .email(email)
                .name("김민수")
                .profileImageUrl("google.com")
                .build();

        // when
        when(oAuthService.login(any(UserPlatformType.class), any(String.class), any(String.class))).thenReturn(oAuthResponse); // Mocking

        AuthServiceLoginResponse login = authService.login(platformType, code, state);     // 로그인 프로세스
        User findUser = userRepository.findByEmail(email).get();                  // 로그인한 사용자 찾기
        boolean tokenValid = jwtService.isTokenValid(login.getToken(), findUser);   // 발행한 토큰 검증

        // then
        assertThat(tokenValid).isTrue();
        assertThat(findUser)
                .extracting("name", "email", "profileImageUrl", "role")
                .contains("김민수", email, "google.com", UserRole.USER);
    }

    @Test
    @DisplayName("User로 등록되어있더라도, 인증이 완료되지 않은 유저(UserRole = UNAUTH)는 로그인할 수 없다.")
    void loginWhenUnAuthUser() {
        // given
        UserPlatformType platformType = UserPlatformType.KAKAO;
        String code = "임의의 코드입니다.";
        String state = "임의의 state입니다.";
        String email = "kimminsu@dankook.ac.kr";

        User user = User.builder()
                .name("김민수")
                .email(email)
                .role(UserRole.UNAUTH)
                .build();
        userRepository.save(user);

        OAuthResponse oAuthResponse = OAuthResponse.builder()
                .platformId("123")
                .email(email)
                .name("김민수")
                .profileImageUrl("google.com")
                .build();

        // when
        when(oAuthService.login(any(UserPlatformType.class), any(String.class), any(String.class))).thenReturn(oAuthResponse); // Mocking

        // then
        assertAll(() -> {
            assertThat(authService.login(platformType, code, state).getRole()).isEqualTo(UserRole.UNAUTH);
            assertThat(authService.login(platformType, code, state).getToken()).isNull();
        });
    }

    @Test
    @DisplayName("로그인이 완료된 사용자의 jwt 토큰에는 알맞은 Claims가 들어가있어야 한다.")
    void loginTokenValidClaims() {
        // given
        UserPlatformType platformType = UserPlatformType.KAKAO;

        User user1 = User.builder()
                .name("김민수")
                .email("abc1")
                .role(UserRole.USER)
                .build();
        User user2 = User.builder()
                .name("김민목")
                .email("abc2")
                .role(UserRole.CHEMIST)
                .build();
        User user3 = User.builder()
                .name("김민금")
                .email("abc3")
                .role(UserRole.CENTER)
                .build();
        userRepository.saveAll(List.of(user1, user2, user3));

        OAuthResponse oAuthResponse1 = OAuthResponse.builder()
                .platformId("111")
                .email("abc1")
                .name("김민수")
                .profileImageUrl("google.com")
                .build();
        OAuthResponse oAuthResponse2 = OAuthResponse.builder()
                .platformId("222")
                .email("abc2")
                .name("김민목")
                .profileImageUrl("google.com")
                .build();
        OAuthResponse oAuthResponse3 = OAuthResponse.builder()
                .platformId("333")
                .email("abc3")
                .name("김민금")
                .profileImageUrl("google.com")
                .build();

        // when
        when(oAuthService.login(any(UserPlatformType.class), eq("abc1"), eq("abc1"))).thenReturn(oAuthResponse1); // Mocking
        when(oAuthService.login(any(UserPlatformType.class), eq("abc2"), eq("abc2"))).thenReturn(oAuthResponse2); // Mocking
        when(oAuthService.login(any(UserPlatformType.class), eq("abc3"), eq("abc3"))).thenReturn(oAuthResponse3); // Mocking

        AuthServiceLoginResponse abc1 = authService.login(platformType, "abc1", "abc1");        // 김민수
        AuthServiceLoginResponse abc2 = authService.login(platformType, "abc2", "abc2");        // 김민목
        AuthServiceLoginResponse abc3 = authService.login(platformType, "abc3", "abc3");        // 김민금

        Claims claims1 = jwtService.extractAllClaims(abc1.getToken());
        Claims claims2 = jwtService.extractAllClaims(abc2.getToken());
        Claims claims3 = jwtService.extractAllClaims(abc3.getToken());

        // then
        assertAll(
                // 김민수
                () -> assertThat(claims1.get("role")).isEqualTo("USER"),
                () -> assertThat(claims1.get("name")).isEqualTo("김민수"),
                () -> assertThat(claims1.get("profileImageUrl")).isEqualTo("google.com"),
                // 김민목
                () -> assertThat(claims2.get("role")).isEqualTo("CHEMIST"),
                () -> assertThat(claims2.get("name")).isEqualTo("김민목"),
                () -> assertThat(claims2.get("profileImageUrl")).isEqualTo("google.com"),
                // 김민금
                () -> assertThat(claims3.get("role")).isEqualTo("CENTER"),
                () -> assertThat(claims3.get("name")).isEqualTo("김민금"),
                () -> assertThat(claims3.get("profileImageUrl")).isEqualTo("google.com")
        );
    }

    @Test
    @DisplayName("기존 사용자의 정보가 바뀌면 수정되어야한다. (별명, 프로필 사진)")
    void loginUpdateUserInfoWhenUserInfoChanged() {
        // given
        UserPlatformType platformType = KAKAO;
        User user = User.builder()
                .platformId("12345")
                .platformType(platformType)
                .email("kms@kakao.com")
                .name("김민수")
                .profileImageUrl("google.co.kr")
                .role(UserRole.USER)
                .build();
        userRepository.save(user);
        OAuthResponse oAuthResponse = OAuthResponse.builder()
                .platformId("12345")
                .platformType(platformType)
                .email("kms@kakao.com")
                .name("김민수짱")
                .profileImageUrl("google.com")
                .build();
        when(oAuthService.login(any(UserPlatformType.class), any(String.class), any(String.class))).thenReturn(oAuthResponse);

        // when
        authService.login(platformType, "123344", "state");
        User findUser = userRepository.findByEmail("kms@kakao.com").get();

        // then
        assertAll(() -> {
            assertThat(findUser.getName()).isEqualTo("김민수짱");
            assertThat(findUser.getProfileImageUrl()).isEqualTo("google.com");
        });
    }
}